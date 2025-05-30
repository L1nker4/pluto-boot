package com.github.pluto.boot.base.service.impl;

import com.github.pluto.boot.base.entity.RoleMenu;
import com.github.pluto.boot.base.mapper.RoleMenuMapper;
import com.github.pluto.boot.base.service.RoleMenuServie;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.List;

import static com.github.pluto.boot.base.entity.table.RoleMenuTableDef.ROLE_MENU;

@Service("roleMenuService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuServie {

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteRoleMenusByRoleId(String[] roleIds) {
		List<String> list = Arrays.asList(roleIds);
		mapper.deleteByCondition(ROLE_MENU.ROLE_ID.in(list));
	}

	@Override
    @Transactional(rollbackFor = Exception.class)
	public void deleteRoleMenusByMenuId(String[] menuIds) {
		List<String> list = Arrays.asList(menuIds);
		mapper.deleteByCondition(ROLE_MENU.MENU_ID.in(list));
	}

	@Override
	public List<RoleMenu> getRoleMenusByRoleId(String roleId) {
		return mapper.selectListByCondition(ROLE_MENU.ROLE_ID.eq(roleId));
	}

}
