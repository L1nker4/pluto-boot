package com.github.pluto.boot.base.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wf.captcha.ArithmeticCaptcha;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wang.l1n.platform.common.annotation.Limit;
import wang.l1n.platform.common.authentication.JWTToken;
import wang.l1n.platform.common.authentication.JWTUtil;
import wang.l1n.platform.common.entity.ActiveUser;
import wang.l1n.platform.common.entity.CommonResult;
import wang.l1n.platform.common.entity.ForestConstant;
import wang.l1n.platform.common.exception.ForestException;
import wang.l1n.platform.common.exception.RedisConnectException;
import wang.l1n.platform.common.properties.ForestProperties;
import wang.l1n.platform.common.service.RedisService;
import wang.l1n.platform.common.utils.*;
import wang.l1n.platform.system.dao.LoginLogMapper;
import wang.l1n.platform.system.entity.LoginLog;
import wang.l1n.platform.system.entity.User;
import wang.l1n.platform.system.entity.UserConfig;
import wang.l1n.platform.system.entity.request.LoginUserRequest;
import wang.l1n.platform.system.manager.UserManager;
import wang.l1n.platform.system.service.LoginLogService;
import wang.l1n.platform.system.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.*;

@Validated
@RestController
public class LoginController {

    @Autowired
    private RedisService redisService;
    @Autowired
    private UserManager userManager;
    @Autowired
    private UserService userService;
    @Autowired
    private LoginLogService loginLogService;
    @Autowired
    private LoginLogMapper loginLogMapper;
    @Autowired
    private ForestProperties properties;
    @Autowired
    private ObjectMapper mapper;

    @PostMapping("/login")
    @Limit(key = "login", period = 60, count = 20, name = "登录接口", prefix = "limit")
    public CommonResult login(@RequestBody LoginUserRequest loginUserRequest,
                              HttpServletRequest request) throws Exception {
        String username = StringUtils.lowerCase(loginUserRequest.getUsername());
        String password = MD5Util.encrypt(username, loginUserRequest.getPassword());
        String uuid = loginUserRequest.getUuid();
        String code = loginUserRequest.getCode();
        //首先校验验证码是否正确
        String rightCode = redisService.get(uuid);
        redisService.del(uuid);
        if (StringUtils.isBlank(rightCode)){
            throw new ForestException("验证码不存在或已过期");
        }
        if (StringUtils.isBlank(code) || !code.equalsIgnoreCase(rightCode)){
            throw new ForestException("验证码错误");
        }

        final String errorMessage = "用户名或密码错误";
        User user = this.userManager.getUser(username);

        if (user == null) {
            throw new ForestException(errorMessage);
        }
        if (!StringUtils.equals(user.getPassword(), password)) {
            throw new ForestException(errorMessage);
        }
        if (User.STATUS_LOCK.equals(user.getStatus())) {
            throw new ForestException("账号已被锁定,请联系管理员！");
        }

        // 更新用户登录时间
        this.userService.updateLoginTime(username);
        // 保存登录记录
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(username);
        this.loginLogService.saveLoginLog(loginLog);

        String token = ForestUtil.encryptToken(JWTUtil.sign(username, password));
        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(properties.getShiro().getJwtTimeOut());
        String expireTimeStr = DateUtil.formatFullTime(expireTime);
        JWTToken jwtToken = new JWTToken(token, expireTimeStr);

        String userId = this.saveTokenToRedis(user, jwtToken, request);
        user.setId(userId);

        Map<String, Object> userInfo = this.generateUserInfo(jwtToken, user);
        return new CommonResult().success("认证成功").data(userInfo);
    }

    @ApiOperation("获取验证码")
    @GetMapping(value = "/code")
    public ResponseEntity<Object> getCode() throws RedisConnectException {
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(111, 36);
        // 几位数运算，默认是两位
        captcha.setLen(2);
        // 获取运算的结果
        String result = captcha.text();
        String uuid = UUID.randomUUID().toString();
        String key = ForestConstant.CODE_PREFIX + StringPool.DASH + uuid;
        // 保存
        redisService.set(key, result, (long) (1000 * 60 * 2));
        // 验证码信息
        Map<String, Object> imgResult = new HashMap<String, Object>(2) {{
            put("img", captcha.toBase64());
            put("uuid", key);
        }};
        return ResponseEntity.ok(imgResult);
    }

    @GetMapping("index/{username}")
    public CommonResult index(@NotBlank(message = "{required}") @PathVariable String username) {
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
        User param = new User();
        param.setUsername(username);
        List<Map<String, Object>> lastSevenUserVisitCount = loginLogMapper.findLastSevenDaysVisitCount(param);
        data.put("lastSevenUserVisitCount", lastSevenUserVisitCount);
        return new CommonResult().data(data);
    }

    @RequiresPermissions("user:online")
    @GetMapping("online")
    public CommonResult userOnline(String username) throws Exception {
        String now = DateUtil.formatFullTime(LocalDateTime.now());
        Set<String> userOnlineStringSet = redisService.zrangeByScore(ForestConstant.ACTIVE_USERS_ZSET_PREFIX, now, "+inf");
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
        return new CommonResult().data(activeUsers);
    }

    @DeleteMapping("kickout/{id}")
    @RequiresPermissions("user:kickout")
    public void kickout(@NotBlank(message = "{required}") @PathVariable String id) throws Exception {
        String now = DateUtil.formatFullTime(LocalDateTime.now());
        Set<String> userOnlineStringSet = redisService.zrangeByScore(ForestConstant.ACTIVE_USERS_ZSET_PREFIX, now, "+inf");
        ActiveUser kickoutUser = null;
        String kickoutUserString = "";
        for (String userOnlineString : userOnlineStringSet) {
            ActiveUser activeUser = mapper.readValue(userOnlineString, ActiveUser.class);
            if (StringUtils.equals(activeUser.getId(), id)) {
                kickoutUser = activeUser;
                kickoutUserString = userOnlineString;
            }
        }
        if (kickoutUser != null && StringUtils.isNotBlank(kickoutUserString)) {
            // 删除 zset中的记录
            redisService.zrem(ForestConstant.ACTIVE_USERS_ZSET_PREFIX, kickoutUserString);
            // 删除对应的 token缓存
            redisService.del(ForestConstant.TOKEN_CACHE_PREFIX + kickoutUser.getToken() + "." + kickoutUser.getIp());
        }
        userManager.deleteUserRedisCache(id);
    }

    @GetMapping("logout/{id}")
    public void logout(@NotBlank(message = "{required}") @PathVariable String id) throws Exception {
        this.kickout(id);
    }

    @PostMapping("regist")
    public void regist(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password) throws Exception {
        this.userService.regist(username, password);
    }

    private String saveTokenToRedis(User user, JWTToken token, HttpServletRequest request) throws Exception {
        String ip = IPUtil.getIpAddr(request);

        // 构建在线用户
        ActiveUser activeUser = new ActiveUser();
        activeUser.setUsername(user.getUsername());
        activeUser.setIp(ip);
        activeUser.setToken(token.getToken());
        activeUser.setLoginAddress(AddressUtil.getCityInfo(ip));

        // zset 存储登录用户，score 为过期时间戳
        this.redisService.zadd(ForestConstant.ACTIVE_USERS_ZSET_PREFIX, Double.valueOf(token.getExipreAt()), mapper.writeValueAsString(activeUser));
        // redis 中存储这个加密 token，key = 前缀 + 加密 token + .ip
        this.redisService.set(ForestConstant.TOKEN_CACHE_PREFIX + token.getToken() + StringPool.DOT + ip, token.getToken(), properties.getShiro().getJwtTimeOut() * 1000);

        return activeUser.getId();
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
    private Map<String, Object> generateUserInfo(JWTToken token, User user) {
        String username = user.getUsername();
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("token", token.getToken());
        userInfo.put("exipreTime", token.getExipreAt());

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
