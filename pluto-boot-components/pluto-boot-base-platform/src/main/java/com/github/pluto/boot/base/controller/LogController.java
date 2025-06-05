package com.github.pluto.boot.base.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.github.pluto.boot.base.common.QueryRequest;
import com.github.pluto.boot.base.entity.SysLog;
import com.github.pluto.boot.base.exception.BaseException;
import com.github.pluto.boot.base.logging.Log;
import com.github.pluto.boot.base.service.SysLogService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import jodd.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("log")
public class LogController extends BaseController {

    private String message;

    @Autowired
    private SysLogService logService;

    @GetMapping
    @SaCheckPermission("log:view")
    public Map<String, Object> logList(QueryRequest request, SysLog sysLog) {
        return getDataTable(logService.findLogs(request, sysLog));
    }

    @Log("删除系统日志")
    @DeleteMapping("/{ids}")
    @SaCheckPermission("log:delete")
    public void deleteLogss(@NotBlank(message = "{required}") @PathVariable String ids) throws BaseException {
        try {
            String[] logIds = ids.split(StringPool.COMMA);
            this.logService.deleteLogs(logIds);
        } catch (Exception e) {
            message = "删除日志失败";
            log.error(message, e);
            throw new BaseException(message);
        }
    }

    @PostMapping("excel")
    @SaCheckPermission("log:export")
    public void export(QueryRequest request, SysLog sysLog, HttpServletResponse response) throws BaseException {
        try {
            List<SysLog> sysLogs = this.logService.findLogs(request, sysLog).getRecords();
//            ExcelKit.$Export(SysLog.class, response).downXlsx(sysLogs, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new BaseException(message);
        }
    }
}
