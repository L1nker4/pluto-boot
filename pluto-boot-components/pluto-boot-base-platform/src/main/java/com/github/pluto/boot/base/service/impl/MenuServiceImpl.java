package com.github.pluto.boot.base.service.impl;

import com.github.pluto.boot.base.common.BaseSystemConstant;
import com.github.pluto.boot.base.common.Tree;
import com.github.pluto.boot.base.entity.Menu;
import com.github.pluto.boot.base.mapper.MenuMapper;
import com.github.pluto.boot.base.service.MenuService;
import com.github.pluto.boot.base.service.SysUserManager;
import com.github.pluto.boot.base.utils.TreeUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.github.pluto.boot.base.entity.table.MenuTableDef.MENU;

@Slf4j
@Service("menuService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private SysUserManager userManager;

    @Override
    public List<Menu> findUserPermissions(String username) {
        return mapper.findUserPermissions(username);
    }

    @Override
    public List<Menu> findUserMenus(String username) {
        return mapper.findUserMenus(username);
    }

    @Override
    public Map<String, Object> findMenus(Menu menu) {
        Map<String, Object> result = new HashMap<>();
        try {
            QueryWrapper queryWrapper = QueryWrapper.create().select().from(MENU);
            findMenuCondition(queryWrapper, menu);
            List<Menu> menus = mapper.selectListByQuery(queryWrapper);

            List<Tree<Menu>> trees = new ArrayList<>();
            List<String> ids = new ArrayList<>();
            buildTrees(trees, menus, ids);

            result.put("ids", ids);
            if (StringUtils.equals(menu.getType(), BaseSystemConstant.TYPE_BUTTON)) {
                result.put("rows", trees);
            } else {
                Tree<Menu> menuTree = TreeUtil.build(trees);
                result.put("rows", menuTree);
            }

            result.put("total", menus.size());
        } catch (NumberFormatException e) {
            log.error("查询菜单失败", e);
            result.put("rows", null);
            result.put("total", 0);
        }
        return result;
    }


    @Override
    public List<Menu> findMenuList(Menu menu) {
        QueryWrapper queryWrapper = QueryWrapper.create().select().from(MENU);
        findMenuCondition(queryWrapper, menu);
        queryWrapper.orderBy(MENU.MENU_ID, true);
        return mapper.selectListByQuery(queryWrapper);
    }

    @Override
    @Transactional
    public void createMenu(Menu menu) {
        menu.setCreateTime(new Date());
        setMenu(menu);
        this.save(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(Menu menu) throws Exception {
        menu.setUpdateTime(new Date());
        setMenu(menu);
        mapper.update(menu);

        // 查找与这些菜单/按钮关联的用户
        List<String> userIds = this.mapper.findUserIdsByMenuId(String.valueOf(menu.getMenuId()));
        // 重新将这些用户的角色和权限缓存到 Redis中
        this.userManager.loadUserPermissionRoleRedisCache(userIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMeuns(String[] menuIds) throws Exception {
        this.delete(Arrays.asList(menuIds));
        for (String menuId : menuIds) {
            // 查找与这些菜单/按钮关联的用户
            List<String> userIds = this.mapper.findUserIdsByMenuId(String.valueOf(menuId));
            // 重新将这些用户的角色和权限缓存到 Redis中
            this.userManager.loadUserPermissionRoleRedisCache(userIds);
        }
    }

    private void buildTrees(List<Tree<Menu>> trees, List<Menu> menus, List<String> ids) {
        menus.forEach(menu -> {
            ids.add(menu.getMenuId().toString());
            Tree<Menu> tree = new Tree<>();
            tree.setId(menu.getMenuId().toString());
            tree.setKey(tree.getId());
            tree.setParentId(menu.getParentId().toString());
            tree.setText(menu.getMenuName());
            tree.setTitle(tree.getText());
            tree.setIcon(menu.getIcon());
            tree.setComponent(menu.getComponent());
            tree.setCreateTime(menu.getCreateTime());
            tree.setUpdateTime(menu.getUpdateTime());
            tree.setPath(menu.getPath());
            tree.setOrder(menu.getSort());
            tree.setPermission(menu.getPerms());
            tree.setType(menu.getType());
            trees.add(tree);
        });
    }

    private void setMenu(Menu menu) {
        if (menu.getParentId() == null) {
            menu.setParentId(0L);
        }
        if (Menu.TYPE_BUTTON.equals(menu.getType())) {
            menu.setPath(null);
            menu.setIcon(null);
            menu.setComponent(null);
        }
    }

    private void findMenuCondition(QueryWrapper queryWrapper, Menu menu) {
        queryWrapper
                .and(MENU.MENU_NAME.eq(menu.getMenuName())
                        .when(StringUtils.isNotBlank(menu.getMenuName())))
                .and(MENU.TYPE.eq(menu.getType())
                        .when(StringUtils.isNotBlank(menu.getType())))
                .and(MENU.CREATE_TIME.between(menu.getCreateTimeFrom(), menu.getCreateTimeTo())
                        .when(StringUtils.isNotBlank(menu.getCreateTimeFrom()) && StringUtils.isNotBlank(menu.getCreateTimeTo())));
    }


    private void delete(List<String> menuIds) {
        removeByIds(menuIds);

        QueryWrapper queryWrapper = QueryWrapper.create();
        queryWrapper.in(Menu::getParentId, menuIds);
        List<Menu> menus = mapper.selectListByQuery(queryWrapper);
        if (CollectionUtils.isNotEmpty(menus)) {
            List<String> menuIdList = new ArrayList<>();
            menus.forEach(m -> menuIdList.add(String.valueOf(m.getMenuId())));
            this.delete(menuIdList);
        }
    }

}
