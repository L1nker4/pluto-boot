package com.github.pluto.boot.base.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.github.pluto.boot.base.common.QueryRequest;
import com.github.pluto.boot.base.entity.Role;
import com.github.pluto.boot.base.entity.RoleMenu;
import com.github.pluto.boot.base.exception.BaseException;
import com.github.pluto.boot.base.logging.Log;
import com.github.pluto.boot.base.service.RoleMenuServie;
import com.github.pluto.boot.base.service.RoleService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jodd.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@RequestMapping("role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleMenuServie roleMenuServie;

    private String message;

    @GetMapping
    @SaCheckPermission("role:view")
    public Map<String, Object> roleList(QueryRequest queryRequest, Role role) {
        return getDataTable(roleService.findRoles(role, queryRequest));
    }

    @GetMapping("check/{roleName}")
    public boolean checkRoleName(@NotBlank(message = "{required}") @PathVariable String roleName) {
        Role result = this.roleService.findByName(roleName);
        return result == null;
    }

    @GetMapping("menu/{roleId}")
    public List<String> getRoleMenus(@NotBlank(message = "{required}") @PathVariable String roleId) {
        List<RoleMenu> list = this.roleMenuServie.getRoleMenusByRoleId(roleId);
        return list.stream().map(roleMenu -> String.valueOf(roleMenu.getMenuId())).collect(Collectors.toList());
    }

    @Log("新增角色")
    @PostMapping
    @SaCheckPermission("role:add")
    public void addRole(@Valid Role role) throws BaseException {
        try {
            this.roleService.createRole(role);
        } catch (Exception e) {
            message = "新增角色失败";
            log.error(message, e);
            throw new BaseException(message);
        }
    }

    @Log("删除角色")
    @DeleteMapping("/{roleIds}")
    @SaCheckPermission("role:delete")
    public void deleteRoles(@NotBlank(message = "{required}") @PathVariable String roleIds) throws BaseException {
        try {
            String[] ids = roleIds.split(StringPool.COMMA);
            this.roleService.deleteRoles(ids);
        } catch (Exception e) {
            message = "删除角色失败";
            log.error(message, e);
            throw new BaseException(message);
        }
    }

    @Log("修改角色")
    @PutMapping
    @SaCheckPermission("role:update")
    public void updateRole(@RequestBody Role role) throws BaseException {
        try {
            this.roleService.updateRole(role);
        } catch (Exception e) {
            message = "修改角色失败";
            log.error(message, e);
            throw new BaseException(message);
        }
    }

    @PostMapping("excel")
    @SaCheckPermission("role:export")
    public void export(QueryRequest queryRequest, Role role, HttpServletResponse response) throws BaseException {
        try {
            List<Role> roles = this.roleService.findRoles(role, queryRequest).getRecords();
//            ExcelKit.$Export(Role.class, response).downXlsx(roles, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new BaseException(message);
        }
    }
}
