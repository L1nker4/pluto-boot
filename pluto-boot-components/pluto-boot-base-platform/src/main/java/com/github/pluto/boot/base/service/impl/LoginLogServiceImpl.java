package com.github.pluto.boot.base.service.impl;


import com.github.pluto.boot.base.entity.LoginLog;
import com.github.pluto.boot.base.mapper.LoginLogMapper;
import com.github.pluto.boot.base.service.LoginLogService;
import com.github.pluto.boot.base.utils.AddressUtil;
import com.github.pluto.boot.web.utils.HttpContextUtil;
import com.github.pluto.boot.web.utils.IPUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author     ：L1nker4
 * @date       ： 创建于  2020/1/19 10:01
 * @description： 
 */
@Service("loginLogService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveLoginLog(LoginLog loginLog) {
        loginLog.setLoginTime(new Date());
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        String ip = IPUtil.getIpAddr(request);
        loginLog.setIp(ip);
        loginLog.setLocation(AddressUtil.getCityInfo(ip));
        this.save(loginLog);
    }
}
