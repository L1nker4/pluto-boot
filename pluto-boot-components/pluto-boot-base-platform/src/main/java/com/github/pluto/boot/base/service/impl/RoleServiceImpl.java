package com.github.pluto.boot.base.service.impl;

import com.github.pluto.boot.base.common.QueryRequest;
import com.github.pluto.boot.base.entity.Role;
import com.github.pluto.boot.base.entity.RoleMenu;
import com.github.pluto.boot.base.mapper.RoleMapper;
import com.github.pluto.boot.base.mapper.RoleMenuMapper;
import com.github.pluto.boot.base.service.RoleMenuServie;
import com.github.pluto.boot.base.service.RoleService;
import com.github.pluto.boot.base.service.SysUserManager;
import com.github.pluto.boot.base.service.UserRoleService;
import com.github.pluto.boot.base.utils.SortUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import jodd.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.github.pluto.boot.base.entity.table.RoleMenuTableDef.ROLE_MENU;
import static com.github.pluto.boot.base.entity.table.RoleTableDef.ROLE;

@Slf4j
@Service("roleService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleMenuServie roleMenuService;
    @Autowired
    private SysUserManager userManager;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Page<Role> findRoles(Role role, QueryRequest request) {
        try {

            QueryWrapper queryWrapper = QueryWrapper.create().select().from(ROLE);
            queryWrapper
                    .where(ROLE.ROLE_NAME.eq(role.getRoleName())
                            .when(StringUtils.isNotBlank(role.getRoleName())))
                    .and(ROLE.CREATE_TIME.between(role.getCreateTimeFrom(), role.getCreateTimeTo())
                            .when(StringUtils.isNotBlank(role.getCreateTimeFrom()) && StringUtils.isNotBlank(role.getCreateTimeTo())));

            Page<Role> page = new Page<>();
            SortUtil.handlePageSort(request, queryWrapper, page, true);
            return this.page(page,queryWrapper);
        } catch (Exception e) {
            log.error("获取角色信息失败", e);
            return null;
        }
    }

    @Override
    public List<Role> findUserRole(String userName) {
        return mapper.findUserRole(userName);
    }

    @Override
    public Role findByName(String roleName) {
        return mapper.selectOneByCondition(ROLE.ROLE_NAME.eq(roleName));
    }

    @Override
    public void createRole(Role role) {
        role.setCreateTime(new Date());
        this.save(role);

        String[] menuIds = role.getMenuId().split(StringPool.COMMA);
        setRoleMenus(role, menuIds);
    }

    @Override
    public void deleteRoles(String[] roleIds) throws Exception {
        // 查找这些角色关联了那些用户
        List<String> userIds = this.userRoleService.findUserIdsByRoleId(roleIds);

        List<String> list = Arrays.asList(roleIds);

        mapper.deleteBatchByIds(list);

        this.roleMenuService.deleteRoleMenusByRoleId(roleIds);
        this.userRoleService.deleteUserRolesByRoleId(roleIds);

        // 重新将这些用户的角色和权限缓存到 Redis中
        this.userManager.loadUserPermissionRoleRedisCache(userIds);

    }

    @Override
    public void updateRole(Role role) throws Exception {
        // 查找这些角色关联了那些用户
        String[] roleId = {String.valueOf(role.getRoleId())};
        List<String> userIds = this.userRoleService.findUserIdsByRoleId(roleId);

        role.setUpdateTime(new Date());
        roleMapper.update(role);

        roleMenuMapper.deleteByCondition(ROLE_MENU.ROLE_ID.eq(role.getRoleId()));

        String[] menuIds = role.getMenuId().split(StringPool.COMMA);
        setRoleMenus(role, menuIds);

        // 重新将这些用户的角色和权限缓存到 Redis中
        this.userManager.loadUserPermissionRoleRedisCache(userIds);
    }

    private void setRoleMenus(Role role, String[] menuIds) {
        Arrays.stream(menuIds).forEach(menuId -> {
            RoleMenu rm = new RoleMenu();
            rm.setMenuId(Long.valueOf(menuId));
            rm.setRoleId(role.getRoleId());
            this.roleMenuMapper.insert(rm);
        });
    }
}
