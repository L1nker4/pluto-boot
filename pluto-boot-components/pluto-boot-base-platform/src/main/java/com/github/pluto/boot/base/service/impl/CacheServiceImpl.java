package com.github.pluto.boot.base.service.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pluto.boot.base.common.BaseSystemConstant;
import com.github.pluto.boot.base.entity.Menu;
import com.github.pluto.boot.base.entity.Role;
import com.github.pluto.boot.base.entity.SysUser;
import com.github.pluto.boot.base.entity.UserConfig;
import com.github.pluto.boot.base.exception.BaseException;
import com.github.pluto.boot.base.mapper.SysUserMapper;
import com.github.pluto.boot.base.service.*;
import com.github.pluto.boot.cache.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("cacheService")
public class CacheServiceImpl implements CacheService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private SysUserService userService;

    @Autowired
    private UserConfigService userConfigService;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public void testConnect() throws Exception {
        this.redisService.exists("test");
    }

    @Override
    public SysUser getUser(String username) throws Exception {
        String userString = this.redisService.get(BaseSystemConstant.USER_CACHE_PREFIX + username);
        if (StringUtils.isEmpty(userString)) {
            throw new BaseException("缓存未击中");
        } else {
            return this.mapper.readValue(userString, SysUser.class);
        }
    }

    @Override
    public List<Role> getRoles(String username) throws Exception {
        String roleListString = this.redisService.get(BaseSystemConstant.USER_ROLE_CACHE_PREFIX + username);
        if (StringUtils.isNotBlank(roleListString)) {
            JavaType type = mapper.getTypeFactory().constructParametricType(List.class, Role.class);
            return this.mapper.readValue(roleListString, type);
        }
        return null;
    }

    @Override
    public List<Menu> getPermissions(String username) throws Exception {
        String permissionListString = this.redisService.get(BaseSystemConstant.USER_PERMISSION_CACHE_PREFIX + username);
        if (StringUtils.isNotBlank(permissionListString)) {
            JavaType type = mapper.getTypeFactory().constructParametricType(List.class, Menu.class);
            return this.mapper.readValue(permissionListString, type);
        }
        return null;
    }

    @Override
    public UserConfig getUserConfig(String userId) throws Exception {
        String userConfigString = this.redisService.get(BaseSystemConstant.USER_CONFIG_CACHE_PREFIX + userId);
        if (StringUtils.isNotBlank(userConfigString)) {
            return this.mapper.readValue(userConfigString, UserConfig.class);
        }
        return null;
    }

    @Override
    public void saveUser(SysUser user) throws Exception {
        String username = user.getUsername();
        this.deleteUser(username);
        redisService.set(BaseSystemConstant.USER_CACHE_PREFIX + username, mapper.writeValueAsString(user));
    }

    @Override
    public void saveUser(String username) throws Exception {
        SysUser user = userMapper.findDetail(username);
        this.deleteUser(username);
        redisService.set(BaseSystemConstant.USER_CACHE_PREFIX + username, mapper.writeValueAsString(user));
    }

    @Override
    public void saveRoles(String username) throws Exception {
        List<Role> roleList = this.roleService.findUserRole(username);
        if (!roleList.isEmpty()) {
            this.deleteRoles(username);
            redisService.set(BaseSystemConstant.USER_ROLE_CACHE_PREFIX + username, mapper.writeValueAsString(roleList));
        }

    }

    @Override
    public void savePermissions(String username) throws Exception {
        List<Menu> permissionList = this.menuService.findUserPermissions(username);
        if (!permissionList.isEmpty()) {
            this.deletePermissions(username);
            redisService.set(BaseSystemConstant.USER_PERMISSION_CACHE_PREFIX + username, mapper.writeValueAsString(permissionList));
        }
    }

    @Override
    public void saveUserConfigs(String userId) throws Exception {
        UserConfig userConfig = this.userConfigService.findByUserId(userId);
        if (userConfig != null) {
            this.deleteUserConfigs(userId);
            redisService.set(BaseSystemConstant.USER_CONFIG_CACHE_PREFIX + userId, mapper.writeValueAsString(userConfig));
        }
    }

    @Override
    public void deleteUser(String username) throws Exception {
        username = username.toLowerCase();
        redisService.del(BaseSystemConstant.USER_CACHE_PREFIX + username);
    }

    @Override
    public void deleteRoles(String username) throws Exception {
        username = username.toLowerCase();
        redisService.del(BaseSystemConstant.USER_ROLE_CACHE_PREFIX + username);
    }

    @Override
    public void deletePermissions(String username) throws Exception {
        username = username.toLowerCase();
        redisService.del(BaseSystemConstant.USER_PERMISSION_CACHE_PREFIX + username);
    }

    @Override
    public void deleteUserConfigs(String userId) throws Exception {
        redisService.del(BaseSystemConstant.USER_CONFIG_CACHE_PREFIX + userId);
    }
}
