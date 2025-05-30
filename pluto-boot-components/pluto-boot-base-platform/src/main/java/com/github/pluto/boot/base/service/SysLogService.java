package com.github.pluto.boot.base.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pluto.boot.base.common.QueryRequest;
import com.github.pluto.boot.base.entity.SysLog;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.scheduling.annotation.Async;



public interface SysLogService extends IService<SysLog> {

    Page<SysLog> findLogs(QueryRequest request, SysLog sysLog);

    void deleteLogs(String[] logIds);

    @Async
    void saveLog(ProceedingJoinPoint point, SysLog log) throws JsonProcessingException;
}
