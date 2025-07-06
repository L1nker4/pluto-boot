package com.github.pluto.boot.base.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pluto.boot.base.common.BaseSystemConstant;
import com.github.pluto.boot.base.entity.ActiveUser;
import com.github.pluto.boot.base.entity.LoginLog;
import com.github.pluto.boot.base.entity.SysUser;
import com.github.pluto.boot.base.entity.UserConfig;
import com.github.pluto.boot.base.entity.request.LoginUserRequest;
import com.github.pluto.boot.base.mapper.LoginLogMapper;
import com.github.pluto.boot.base.service.LoginLogService;
import com.github.pluto.boot.base.service.SysUserManager;
import com.github.pluto.boot.base.service.SysUserService;
import com.github.pluto.boot.base.utils.DateUtil;
import com.github.pluto.boot.base.utils.MD5Util;
import com.github.pluto.boot.cache.exception.RedisConnectException;
import com.github.pluto.boot.cache.service.RedisService;
import com.github.pluto.boot.rate.limiter.annotation.ApiRateLimit;
import com.github.pluto.boot.web.entity.CommonResult;
import com.github.pluto.boot.web.exception.PlutoException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jodd.util.StringPool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@Validated
@RestController
@Tag(name = "系统登录模块")
public class LoginController {

    @Autowired
    private RedisService redisService;
    @Autowired
    private SysUserManager userManager;
    @Autowired
    private SysUserService userService;
    @Autowired
    private LoginLogService loginLogService;
    @Autowired
    private LoginLogMapper loginLogMapper;

    @Autowired
    private ObjectMapper mapper;

    @PostMapping("/login")
    @ApiRateLimit(key = "login", period = 60, count = 10, name = "登录接口", prefix = "limit")
    public CommonResult<Map<String, Object>> login(@RequestBody LoginUserRequest loginUserRequest,
                              HttpServletRequest request) throws Exception {
        String username = StringUtils.lowerCase(loginUserRequest.getUsername());
        String password = MD5Util.encrypt(username, loginUserRequest.getPassword());
        String uuid = loginUserRequest.getUuid();
        String code = loginUserRequest.getCode();

        //首先校验验证码是否正确
        String rightCode = redisService.get(uuid);
        redisService.del(uuid);
        if (StringUtils.isBlank(rightCode)){
            throw new PlutoException("验证码不存在或已过期");
        }
        if (StringUtils.isBlank(code) || !code.equalsIgnoreCase(rightCode)){
            throw new PlutoException("验证码错误");
        }

        final String errorMessage = "用户名或密码错误";
        SysUser user = this.userManager.getUser(username);

        if (user == null) {
            throw new PlutoException(errorMessage);
        }
        if (!StringUtils.equals(user.getPassword(), password)) {
            throw new PlutoException(errorMessage);
        }
        if (SysUser.STATUS_LOCK.equals(user.getStatus())) {
            throw new PlutoException("账号已被锁定,请联系管理员！");
        }

        // 更新用户登录时间
        this.userService.updateLoginTime(username);
        // 保存登录记录
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(username);
        this.loginLogService.saveLoginLog(loginLog);

        StpUtil.login(user.getUsername(), BaseSystemConstant.SYS_USER);
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        Map<String, Object> userInfo = this.generateUserInfo(tokenInfo, user);
        return new CommonResult<Map<String, Object>>().success("认证成功").data(userInfo);
    }

    @Operation(summary = "获取验证码")
    @GetMapping(value = "/code")
    public CommonResult<Map<String, Object>> getCode() throws RedisConnectException {
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(130, 48, 4, 10);

        // 获取运算的结果
        String result = captcha.getCode();
        String uuid = UUID.randomUUID().toString();
        String redisKey = BaseSystemConstant.CODE_PREFIX + StringPool.DASH + uuid;
        // 保存
        redisService.set(redisKey, result, 60 * 5L);

        Map<String, Object> imgResult = Map.of(
                "img", captcha.getImageBase64(),
                "uuid", redisKey
        );
        return new CommonResult<Map<String, Object>>().data(imgResult);
    }

    @GetMapping("index/{username}")
    public CommonResult<Map<String, Object>> index(@NotBlank(message = "{required}") @PathVariable String username) {
        Map<String, Object> data = new HashMap<>();
        // 获取系统访问记录
        Long totalVisitCount = loginLogMapper.findTotalVisitCount();
        data.put("totalVisitCount", totalVisitCount);
        Long todayVisitCount = loginLogMapper.findTodayVisitCount();
        data.put("todayVisitCount", todayVisitCount);
        Long todayIp = loginLogMapper.findTodayIp();
        data.put("todayIp", todayIp);
        // 获取近期系统访问记录
        List<Map<String, Object>> lastSevenVisitCount = loginLogMapper.findLastSevenDaysVisitCount(null);
        data.put("lastSevenVisitCount", lastSevenVisitCount);
        SysUser param = new SysUser();
        param.setUsername(username);
        List<Map<String, Object>> lastSevenUserVisitCount = loginLogMapper.findLastSevenDaysVisitCount(param);
        data.put("lastSevenUserVisitCount", lastSevenUserVisitCount);
        return new CommonResult<Map<String, Object>>().data(data);
    }

    @SaCheckPermission("user:online")
    @GetMapping("online")
    public CommonResult<List<ActiveUser>> userOnline(String username) throws Exception {
        String now = DateUtil.formatFullTime(LocalDateTime.now());
        Double nowScore = Double.parseDouble(now);
        Set<String> userOnlineStringSet = redisService.zrangeByScore(BaseSystemConstant.ACTIVE_USERS_ZSET_PREFIX, nowScore, Double.POSITIVE_INFINITY);
        List<ActiveUser> activeUsers = new ArrayList<>();
        for (String userOnlineString : userOnlineStringSet) {
            ActiveUser activeUser = mapper.readValue(userOnlineString, ActiveUser.class);
            activeUser.setToken(null);
            if (StringUtils.isNotBlank(username)) {
                if (StringUtils.equalsIgnoreCase(username, activeUser.getUsername())) {
                    activeUsers.add(activeUser);
                }
            } else {
                activeUsers.add(activeUser);
            }
        }
        return new CommonResult<List<ActiveUser>>().data(activeUsers);
    }

    @DeleteMapping("kickout/{id}")
    @SaCheckPermission("user:kickout")
    public void kickout(@NotBlank(message = "{required}") @PathVariable String id) throws Exception {
        SysUser sysUser = userService.getById(id);
        if (sysUser == null) {
            throw new PlutoException("用户不存在");
        }
        StpUtil.logout(sysUser.getUsername(), BaseSystemConstant.SYS_USER);
    }

    @GetMapping("logout/{id}")
    public void logout(@NotBlank(message = "{required}") @PathVariable String id) throws Exception {
        this.kickout(id);
    }

    @PostMapping("regist")
    public void register(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password) throws Exception {
        this.userService.regist(username, password);
    }

    /**
     * 生成前端需要的用户信息，包括：
     * 1. token
     * 2. Vue Router
     * 3. 用户角色
     * 4. 用户权限
     * 5. 前端系统个性化配置信息
     *
     * @param token token
     * @param user  用户信息
     * @return UserInfo
     */
    private Map<String, Object> generateUserInfo(SaTokenInfo token, SysUser user) {
        String username = user.getUsername();
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("token", token.getTokenValue());
        userInfo.put("exipreTime", System.currentTimeMillis() / 1000 + token.getTokenTimeout());

        Set<String> roles = this.userManager.getUserRoles(username);
        userInfo.put("roles", roles);

        Set<String> permissions = this.userManager.getUserPermissions(username);
        userInfo.put("permissions", permissions);

        UserConfig userConfig = this.userManager.getUserConfig(String.valueOf(user.getUserId()));
        userInfo.put("config", userConfig);

        user.setPassword("it's a secret");
        userInfo.put("user", user);
        return userInfo;
    }
}
