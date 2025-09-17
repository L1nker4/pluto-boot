package com.github.pluto.boot.base.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.github.pluto.boot.base.common.QueryRequest;
import com.github.pluto.boot.base.entity.Role;
import com.github.pluto.boot.base.entity.SysUser;
import com.github.pluto.boot.base.entity.UserConfig;
import com.github.pluto.boot.base.entity.request.UpdateAvatorRequest;
import com.github.pluto.boot.base.exception.BaseException;
import com.github.pluto.boot.base.logging.Log;
import com.github.pluto.boot.base.service.RoleService;
import com.github.pluto.boot.base.service.SysUserService;
import com.github.pluto.boot.base.service.UserConfigService;
import com.github.pluto.boot.base.utils.MD5Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jodd.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Validated
@RestController
@Tag(name = "用户管理")
@RequestMapping("base/user")
public class UserController extends BaseController {

    private String message;

    @Autowired
    private SysUserService userService;
    @Autowired
    private UserConfigService userConfigService;
    @Autowired
    private RoleService roleService;

    @Operation(summary = "检查用户名是否存在")
    @GetMapping("check/{username}")
    public boolean checkUserName(@NotBlank(message = "{required}") @PathVariable String username) {
        return this.userService.findByName(username) == null;
    }

    @Operation(summary = "获取用户详情")
    @GetMapping("/{username}")
    public SysUser detail(@NotBlank(message = "{required}") @PathVariable String username) {
        SysUser user = this.userService.findByName(username);
        //修复用户修改自己的个人信息第二次提示roleId不能为空
        List<Role> roles = roleService.findUserRole(username);
        String roleIdStr = StringUtils
                .join(roles
                        .stream()
                        .map(Role::getRoleId)
                        .toArray(Long[]::new), ",");
        user.setRoleId(roleIdStr);
        return user;
    }

    @Operation(summary = "获取用户列表")
    @GetMapping
    @SaCheckPermission("user:view")
    public Map<String, Object> userList(QueryRequest queryRequest, SysUser user) {
        return getDataTable(userService.findUserDetail(user, queryRequest));
    }

    @Operation(summary = "新增用户")
    @Log("新增用户")
    @PostMapping
    @SaCheckPermission("user:add")
    public void addUser(@Valid SysUser user) throws BaseException {
        try {
            this.userService.createUser(user);
        } catch (Exception e) {
            message = "新增用户失败";
            log.error(message, e);
            throw new BaseException(message);
        }
    }

    @Operation(summary = "修改用户")
    @Log("修改用户")
    @PutMapping
    @SaCheckPermission("user:update")
    public void updateUser(@Valid SysUser user) throws BaseException {
        try {
            this.userService.updateUser(user);
        } catch (Exception e) {
            message = "修改用户失败";
            log.error(message, e);
            throw new BaseException(message);
        }
    }

    @Operation(summary = "删除用户")
    @Log("删除用户")
    @DeleteMapping("/{userIds}")
    @SaCheckPermission("user:delete")
    public void deleteUsers(@NotBlank(message = "{required}") @PathVariable String userIds) throws BaseException {
        try {
            String[] ids = userIds.split(StringPool.COMMA);
            this.userService.deleteUsers(ids);
        } catch (Exception e) {
            message = "删除用户失败";
            log.error(message, e);
            throw new BaseException(message);
        }
    }

    @Operation(summary = "更新用户个人信息")
    @PutMapping("profile")
    public void updateProfile(@RequestBody @Valid SysUser user) throws BaseException {
        try {
            this.userService.updateProfile(user);
        } catch (Exception e) {
            message = "修改个人信息失败";
            log.error(message, e);
            throw new BaseException(message);
        }
    }

    @Operation(summary = "更新用户头像")
    @PutMapping("avatar")
    public void updateAvatar(@RequestBody @Valid UpdateAvatorRequest request) throws BaseException {
        try {
            this.userService.updateAvatar(request.getUsername(), request.getAvatar());
        } catch (Exception e) {
            message = "修改头像失败";
            log.error(message, e);
            throw new BaseException(message);
        }
    }

    @Operation(summary = "更新用户配置")
    @PutMapping("userconfig")
    public void updateUserConfig(@Valid UserConfig userConfig) throws BaseException {
        try {
            this.userConfigService.update(userConfig);
        } catch (Exception e) {
            message = "修改个性化配置失败";
            log.error(message, e);
            throw new BaseException(message);
        }
    }

    @Operation(summary = "检查密码")
    @GetMapping("password/check")
    public boolean checkPassword(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password) {
        String encryptPassword = MD5Util.encrypt(username, password);
         SysUser user = userService.findByName(username);
        if (user != null) {
            return StringUtils.equals(user.getPassword(), encryptPassword);
        } else {
            return false;
        }
    }

    @Operation(summary = "更新密码")
    @PutMapping("password")
    public void updatePassword(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password) throws BaseException {
        try {
            userService.updatePassword(username, password);
        } catch (Exception e) {
            message = "修改密码失败";
            log.error(message, e);
            throw new BaseException(message);
        }
    }

    @Operation(summary = "重置密码")
    @PutMapping("password/reset")
    @SaCheckPermission("user:reset")
    public void resetPassword(@NotBlank(message = "{required}") String usernames) throws BaseException {
        try {
            String[] usernameArr = usernames.split(StringPool.COMMA);
            this.userService.resetPassword(usernameArr);
        } catch (Exception e) {
            message = "重置用户密码失败";
            log.error(message, e);
            throw new BaseException(message);
        }
    }

    @Operation(summary = "导出用户Excel")
    @PostMapping("excel")
    @SaCheckPermission("user:export")
    public void export(QueryRequest queryRequest, SysUser user, HttpServletResponse response) throws BaseException {
        try {
            List<SysUser> users = this.userService.findUserDetail(user, queryRequest).getRecords();
//            ExcelKit.$Export(User.class, response).downXlsx(users, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new BaseException(message);
        }
    }
}
