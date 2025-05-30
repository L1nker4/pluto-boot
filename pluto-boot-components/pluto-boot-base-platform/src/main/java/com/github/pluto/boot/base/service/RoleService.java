package com.github.pluto.boot.base.service;



import com.github.pluto.boot.base.common.QueryRequest;
import com.github.pluto.boot.base.entity.Role;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import java.util.List;

public interface RoleService extends IService<Role> {

    Page<Role> findRoles(Role role, QueryRequest request);

    List<Role> findUserRole(String userName);

    Role findByName(String roleName);

    void createRole(Role role);

    void deleteRoles(String[] roleIds) throws Exception;

    void updateRole(Role role) throws Exception;
}
