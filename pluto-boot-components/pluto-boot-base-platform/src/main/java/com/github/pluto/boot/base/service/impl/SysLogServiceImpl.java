package com.github.pluto.boot.base.service.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pluto.boot.base.common.BaseSystemConstant;
import com.github.pluto.boot.base.common.QueryRequest;
import com.github.pluto.boot.base.entity.SysLog;
import com.github.pluto.boot.base.logging.Log;
import com.github.pluto.boot.base.mapper.LogMapper;
import com.github.pluto.boot.base.service.SysLogService;
import com.github.pluto.boot.base.utils.AddressUtil;
import com.github.pluto.boot.base.utils.SortUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

import static com.github.pluto.boot.base.entity.table.SysLogTableDef.SYS_LOG;

@Slf4j
@Service("logService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysLogServiceImpl extends ServiceImpl<LogMapper, SysLog> implements SysLogService {

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public Page<SysLog> findLogs(QueryRequest request, SysLog sysLog) {
        try {
            QueryWrapper queryWrapper = QueryWrapper.create()
                    .select().from(SysLog.class)
                    .where(SYS_LOG.USERNAME.eq(sysLog.getUsername()).when(StringUtils.isNotBlank(sysLog.getUsername())))
                    .and(SYS_LOG.OPERATION.like(sysLog.getOperation()).when(StringUtils.isNotBlank(sysLog.getOperation())))
                    .and(SYS_LOG.LOCATION.like(sysLog.getLocation()).when(StringUtils.isNotBlank(sysLog.getLocation())))
                    .and(SYS_LOG.CREATE_TIME.between(sysLog.getCreateTimeFrom(), sysLog.getCreateTimeTo())
                            .when(StringUtils.isNotBlank(sysLog.getCreateTimeFrom()) && StringUtils.isNotBlank(sysLog.getCreateTimeTo())));

            Page<SysLog> page = new Page<>(request.getPageNum(), request.getPageSize());
            SortUtil.handlePageSort(request, queryWrapper, page, "createTime", BaseSystemConstant.ORDER_DESC, true);

            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取系统日志失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void deleteLogs(String[] logIds) {
        List<String> list = Arrays.asList(logIds);
        mapper.deleteBatchByIds(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveLog(ProceedingJoinPoint joinPoint, SysLog log) throws JsonProcessingException {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log logAnnotation = method.getAnnotation(Log.class);
        if (logAnnotation != null) {
            // 注解上的描述
            log.setOperation(logAnnotation.value());
        }
        // 请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        // 请求的方法名
        String methodName = signature.getName();
        log.setMethod(className + "." + methodName + "()");
        // 请求的方法参数值
        Object[] args = joinPoint.getArgs();
        // 请求的方法参数名称
        ParameterNameDiscoverer u = new StandardReflectionParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames(method);
        if (args != null && paramNames != null) {
            StringBuilder params = new StringBuilder();
            params = handleParams(params, args, Arrays.asList(paramNames));
            log.setParams(params.toString());
        }
        log.setCreateTime(new Date());
        log.setLocation(AddressUtil.getCityInfo(log.getIp()));
        // 保存系统日志
        save(log);
    }

    private StringBuilder handleParams(StringBuilder params, Object[] args, List paramNames) throws JsonProcessingException {
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Map) {
                Set set = ((Map) args[i]).keySet();
                List<Object> list = new ArrayList<>();
                List<Object> paramList = new ArrayList<>();
                for (Object key : set) {
                    list.add(((Map) args[i]).get(key));
                    paramList.add(key);
                }
                return handleParams(params, list.toArray(), paramList);
            } else {
                if (args[i] instanceof Serializable) {
                    Class<?> aClass = args[i].getClass();
                    try {
                        aClass.getDeclaredMethod("toString", new Class[]{null});
                        // 如果不抛出 NoSuchMethodException 异常则存在 toString 方法 ，安全的 writeValueAsString ，否则 走 Object的 toString方法
                        params.append(" ").append(paramNames.get(i)).append(": ").append(objectMapper.writeValueAsString(args[i]));
                    } catch (NoSuchMethodException e) {
                        params.append(" ").append(paramNames.get(i)).append(": ").append(objectMapper.writeValueAsString(args[i].toString()));
                    }
                } else if (args[i] instanceof MultipartFile) {
                    MultipartFile file = (MultipartFile) args[i];
                    params.append(" ").append(paramNames.get(i)).append(": ").append(file.getName());
                } else {
                    params.append(" ").append(paramNames.get(i)).append(": ").append(args[i]);
                }
            }
        }
        return params;
    }
}


