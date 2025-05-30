package com.github.pluto.boot.base.service.impl;


import com.github.pluto.boot.base.entity.UserRole;
import com.github.pluto.boot.base.mapper.UserRoleMapper;
import com.github.pluto.boot.base.service.UserRoleService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.pluto.boot.base.entity.table.UserRoleTableDef.USER_ROLE;

@Service("userRoleService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteUserRolesByRoleId(String[] roleIds) {
		Arrays.stream(roleIds).forEach(id -> mapper.deleteByRoleId(Long.valueOf(id)));
	}

	@Override
    @Transactional(rollbackFor = Exception.class)
	public void deleteUserRolesByUserId(String[] userIds) {
		Arrays.stream(userIds).forEach(id -> mapper.deleteByUserId(Long.valueOf(id)));
	}

	@Override
	public List<String> findUserIdsByRoleId(String[] roleIds) {
		List<UserRole> list = mapper.selectListByCondition(USER_ROLE.ROLE_ID.in(String.join(",", roleIds)));
		return list.stream().map(userRole -> String.valueOf(userRole.getUserId())).collect(Collectors.toList());
	}

}
