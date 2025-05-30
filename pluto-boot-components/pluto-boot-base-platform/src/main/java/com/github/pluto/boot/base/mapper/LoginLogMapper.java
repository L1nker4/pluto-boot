package com.github.pluto.boot.base.mapper;


import com.github.pluto.boot.base.entity.LoginLog;
import com.github.pluto.boot.base.entity.SysUser;
import com.mybatisflex.core.BaseMapper;

import java.util.List;
import java.util.Map;

public interface LoginLogMapper extends BaseMapper<LoginLog> {

    /**
     * 获取系统总访问次数
     *
     * @return Long
     */
    Long findTotalVisitCount();

    /**
     * 获取系统今日访问次数
     *
     * @return Long
     */
    Long findTodayVisitCount();

    /**
     * 获取系统今日访问 IP数
     *
     * @return Long
     */
    Long findTodayIp();

    /**
     * 获取系统近七天来的访问记录
     *
     * @param sysUser 用户
     * @return 系统近七天来的访问记录
     */
    List<Map<String, Object>> findLastSevenDaysVisitCount(SysUser sysUser);
}