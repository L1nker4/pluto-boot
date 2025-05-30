package com.github.pluto.boot.base.service;


import com.github.pluto.boot.base.entity.LoginLog;
import com.mybatisflex.core.service.IService;

public interface LoginLogService extends IService<LoginLog> {

    void saveLoginLog(LoginLog loginLog);
}
