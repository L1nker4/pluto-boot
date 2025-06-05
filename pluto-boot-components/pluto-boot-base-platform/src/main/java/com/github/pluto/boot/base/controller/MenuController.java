package com.github.pluto.boot.base.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.github.pluto.boot.base.common.router.VueRouter;
import com.github.pluto.boot.base.entity.Menu;
import com.github.pluto.boot.base.exception.BaseException;
import com.github.pluto.boot.base.logging.Log;
import com.github.pluto.boot.base.service.MenuService;
import com.github.pluto.boot.base.service.SysUserManager;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jodd.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("/menu")
public class MenuController extends BaseController {

    private String message;

    @Autowired
    private SysUserManager userManager;
    @Autowired
    private MenuService menuService;

    @GetMapping("/{username}")
    public ArrayList<VueRouter<Menu>> getUserRouters(@NotBlank(message = "{required}") @PathVariable String username) {
        return this.userManager.getUserRouters(username);
    }

    @GetMapping
    @SaCheckPermission("menu:view")
    public Map<String, Object> menuList(Menu menu) {
        return this.menuService.findMenus(menu);
    }

    @Log("新增菜单/按钮")
    @PostMapping
    @SaCheckPermission("menu:add")
    public void addMenu(@Valid @RequestBody Menu menu) throws BaseException {
        try {
            this.menuService.createMenu(menu);
        } catch (Exception e) {
            message = "新增菜单/按钮失败";
            log.error(message, e);
            throw new BaseException(message);
        }
    }

    @Log("删除菜单/按钮")
    @DeleteMapping("/{menuIds}")
    @SaCheckPermission("menu:delete")
    public void deleteMenus(@NotBlank(message = "{required}") @PathVariable String menuIds) throws BaseException {
        try {
            String[] ids = menuIds.split(StringPool.COMMA);
            this.menuService.deleteMeuns(ids);
        } catch (Exception e) {
            message = "删除菜单/按钮失败";
            log.error(message, e);
            throw new BaseException(message);
        }
    }

    @Log("修改菜单/按钮")
    @PutMapping
    @SaCheckPermission("menu:update")
    public void updateMenu(@Valid @RequestBody Menu menu) throws BaseException {
        try {
            this.menuService.updateMenu(menu);
        } catch (Exception e) {
            message = "修改菜单/按钮失败";
            log.error(message, e);
            throw new BaseException(message);
        }
    }

    @PostMapping("excel")
    @SaCheckPermission("menu:export")
    public void export(Menu menu, HttpServletResponse response) throws BaseException {
        try {
            List<Menu> menus = this.menuService.findMenuList(menu);
//            ExcelKit.$Export(Menu.class, response).downXlsx(menus, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new BaseException(message);
        }
    }
}
