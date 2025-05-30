package com.github.pluto.boot.base.service.impl;


import com.github.pluto.boot.base.common.BaseSystemConstant;
import com.github.pluto.boot.base.common.QueryRequest;
import com.github.pluto.boot.base.entity.SysUser;
import com.github.pluto.boot.base.entity.UserRole;
import com.github.pluto.boot.base.mapper.SysUserMapper;
import com.github.pluto.boot.base.mapper.UserRoleMapper;
import com.github.pluto.boot.base.service.*;
import com.github.pluto.boot.base.utils.MD5Util;
import com.github.pluto.boot.base.utils.SortUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import jodd.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.github.pluto.boot.base.entity.table.SysUserTableDef.SYS_USER;
import static com.github.pluto.boot.base.entity.table.UserRoleTableDef.USER_ROLE;

@Slf4j
@Service("userService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private UserConfigService userConfigService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private SysUserManager userManager;


    @Override
    public SysUser findByName(String username) {
        return mapper.selectOneByCondition(SYS_USER.USERNAME.eq(username));
    }


    @Override
    public Page<SysUser> findUserDetail(SysUser user, QueryRequest request) {
        try {
            Page<SysUser> page = new Page<>();
            QueryWrapper queryWrapper = QueryWrapper.create();

            //TODO
            SortUtil.handlePageSort(request, queryWrapper,page, "userId", BaseSystemConstant.ORDER_ASC, false);
            return mapper.findUserDetail(page, user);
        } catch (Exception e) {
            log.error("查询用户异常", e);
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLoginTime(String username) throws Exception {
        SysUser user = new SysUser();
        user.setLastLoginTime(new Date());

        this.mapper.updateByCondition(user, SYS_USER.USERNAME.eq(username));

        // 重新将用户信息加载到 redis中
        cacheService.saveUser(username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUser(SysUser user) throws Exception {
        // 创建用户
        user.setCreateTime(new Date());
        user.setAvatar(SysUser.DEFAULT_AVATAR);
        user.setPassword(MD5Util.encrypt(user.getUsername(), SysUser.DEFAULT_PASSWORD));
        save(user);

        // 保存用户角色
        String[] roles = user.getRoleId().split(StringPool.COMMA);
        setUserRoles(user, roles);

        // 创建用户默认的个性化配置
        userConfigService.initDefaultUserConfig(String.valueOf(user.getUserId()));

        // 将用户相关信息保存到 Redis中
        userManager.loadUserRedisCache(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(SysUser user) throws Exception {
        // 更新用户
        user.setPassword(null);
        user.setUpdateTime(new Date());
        updateById(user);

        userRoleMapper.deleteByCondition(USER_ROLE.USER_ID.eq(user.getUserId()));

        String[] roles = user.getRoleId().split(StringPool.COMMA);
        setUserRoles(user, roles);

        // 重新将用户信息，用户角色信息，用户权限信息 加载到 redis中
        cacheService.saveUser(user.getUsername());
        cacheService.saveRoles(user.getUsername());
        cacheService.savePermissions(user.getUsername());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUsers(String[] userIds) throws Exception {
        // 先删除相应的缓存
        this.userManager.deleteUserRedisCache(userIds);

        List<String> list = Arrays.asList(userIds);

        removeByIds(list);

        // 删除用户角色
        this.userRoleService.deleteUserRolesByUserId(userIds);
        // 删除用户个性化配置
        this.userConfigService.deleteByUserId(userIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProfile(SysUser user) throws Exception {
        updateById(user);
        // 重新缓存用户信息
        cacheService.saveUser(user.getUsername());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAvatar(String username, String avatar) throws Exception {
        SysUser user = new SysUser();
        user.setAvatar(avatar);

        this.mapper.updateByCondition(user, SYS_USER.USERNAME.eq(username));
        // 重新缓存用户信息
        cacheService.saveUser(username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(String username, String password) throws Exception {
        SysUser user = new SysUser();
        user.setPassword(MD5Util.encrypt(username, password));

        this.mapper.updateByCondition(user, SYS_USER.USERNAME.eq(username));
        // 重新缓存用户信息
        cacheService.saveUser(username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void regist(String username, String password) throws Exception {
        SysUser user = new SysUser();
        user.setPassword(MD5Util.encrypt(username, password));
        user.setUsername(username);
        user.setCreateTime(new Date());
        user.setStatus(SysUser.STATUS_VALID);
        user.setSex(SysUser.SEX_UNKNOW);
        user.setAvatar(SysUser.DEFAULT_AVATAR);
        user.setDescription("注册用户");
        this.save(user);

        UserRole ur = new UserRole();
        ur.setUserId(user.getUserId());
        ur.setRoleId(2L); // 注册用户角色 ID
        this.userRoleMapper.insert(ur);

        // 创建用户默认的个性化配置
        userConfigService.initDefaultUserConfig(String.valueOf(user.getUserId()));
        // 将用户相关信息保存到 Redis中
        userManager.loadUserRedisCache(user);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(String[] usernames) throws Exception {
        for (String username : usernames) {

            SysUser user = new SysUser();
            user.setPassword(MD5Util.encrypt(username, SysUser.DEFAULT_PASSWORD));

            this.mapper.updateByCondition(user, SYS_USER.USERNAME.eq(username));
            // 重新将用户信息加载到 redis中
            cacheService.saveUser(username);
        }

    }

    private void setUserRoles(SysUser user, String[] roles) {
        Arrays.stream(roles).forEach(roleId -> {
            UserRole ur = new UserRole();
            ur.setUserId(user.getUserId());
            ur.setRoleId(Long.valueOf(roleId));
            this.userRoleMapper.insert(ur);
        });
    }
}
