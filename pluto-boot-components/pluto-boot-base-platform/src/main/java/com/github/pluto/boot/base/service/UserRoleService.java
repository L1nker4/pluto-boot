package com.github.pluto.boot.base.service;


import com.github.pluto.boot.base.entity.UserRole;
import com.mybatisflex.core.service.IService;

import java.util.List;

public interface UserRoleService extends IService<UserRole> {

	void deleteUserRolesByRoleId(String[] roleIds);

	void deleteUserRolesByUserId(String[] userIds);

	List<String> findUserIdsByRoleId(String[] roleIds);
}
