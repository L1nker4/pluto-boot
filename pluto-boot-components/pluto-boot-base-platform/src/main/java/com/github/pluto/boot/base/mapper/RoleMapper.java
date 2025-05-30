package com.github.pluto.boot.base.mapper;


import com.github.pluto.boot.base.entity.Role;
import com.mybatisflex.core.BaseMapper;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {
	
	List<Role> findUserRole(String userName);
	
}