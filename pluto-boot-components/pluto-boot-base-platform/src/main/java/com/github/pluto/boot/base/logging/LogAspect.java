package com.github.pluto.boot.base.logging;


import com.github.pluto.boot.base.common.BasePlatformProperties;
import com.github.pluto.boot.base.entity.SysLog;
import com.github.pluto.boot.base.service.SysLogService;
import com.github.pluto.boot.web.utils.HttpContextUtil;
import com.github.pluto.boot.web.utils.IPUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author     ：L1nker4
 * @date       ： 创建于  2020/1/4 14:22
 * @description： 日志切面类
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Resource
    private SysLogService logService;

    @Resource
    private BasePlatformProperties basePlatformProperties;

    @Pointcut("@annotation(com.github.pluto.boot.base.logging.Log)")
    public void pointcut() {
        // do nothing
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        long beginTime = System.currentTimeMillis();
        // 执行方法
        result = point.proceed();
        // 获取 request
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        // 设置 IP 地址
        String ip = IPUtil.getIpAddr(request);
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        if (basePlatformProperties.isOpenAopLog()) {
            // 保存日志

            //todo get username
//            String token = (String) SecurityUtils.getSubject().getPrincipal();
            String username = "";
//            if (StringUtils.isNotBlank(token)) {
//                username = JWTUtil.getUsername(token);
//            }

            SysLog log = new SysLog();
            log.setUsername(username);
            log.setIp(ip);
            log.setTime(time);
            logService.saveLog(point, log);
        }
        return result;
    }
}
