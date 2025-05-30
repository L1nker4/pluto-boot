package com.github.pluto.boot.base.service;

import com.github.pluto.boot.base.entity.RoleMenu;
import com.mybatisflex.core.service.IService;

import java.util.List;

public interface RoleMenuServie extends IService<RoleMenu> {

    void deleteRoleMenusByRoleId(String[] roleIds);

    void deleteRoleMenusByMenuId(String[] menuIds);

    List<RoleMenu> getRoleMenusByRoleId(String roleId);
}
