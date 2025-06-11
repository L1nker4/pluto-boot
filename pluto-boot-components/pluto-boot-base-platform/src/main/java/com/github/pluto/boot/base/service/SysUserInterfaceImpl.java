package com.github.pluto.boot.base.service;

import cn.dev33.satoken.stp.StpInterface;
import com.github.pluto.boot.base.common.BaseSystemConstant;
import com.github.pluto.boot.base.entity.Menu;
import com.github.pluto.boot.base.entity.Role;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SysUserInterfaceImpl implements StpInterface {

    @Resource
    private RoleService roleService;

    @Resource
    private MenuService menuService;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        if (loginType.equals(BaseSystemConstant.SYS_USER)) {
            return getSysUserPermissionList(loginId);
        } else {
            throw new RuntimeException("<UNK>: " + loginType);
        }
    }

    private List<String> getSysUserPermissionList(Object loginId) {
        return menuService.findUserPermissions(String.valueOf(loginId))
                .stream()
                .map(Menu::getPerms)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        if (loginType.equals(BaseSystemConstant.SYS_USER)) {
            return getSysUserRoleList(loginId);
        } else {
            throw new RuntimeException("<UNK>: " + loginType);
        }
    }

    private List<String> getSysUserRoleList(Object loginId) {
        return roleService.findUserRole(String.valueOf(loginId))
                .stream()
                .map(Role::getRoleName)
                .collect(Collectors.toList());
    }
}
