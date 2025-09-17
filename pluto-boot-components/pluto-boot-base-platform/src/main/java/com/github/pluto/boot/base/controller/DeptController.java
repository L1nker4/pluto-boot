package com.github.pluto.boot.base.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.github.pluto.boot.base.common.QueryRequest;
import com.github.pluto.boot.base.entity.Dept;
import com.github.pluto.boot.base.exception.BaseException;
import com.github.pluto.boot.base.logging.Log;
import com.github.pluto.boot.base.service.DeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
@Tag(name = "部门管理")
@RequestMapping("base/dept")
public class DeptController extends BaseController {

    private String message;

    @Autowired
    private DeptService deptService;

    @Operation(summary = "获取部门列表")
    @GetMapping
    public Map<String, Object> deptList(QueryRequest request, Dept dept) {
        return this.deptService.findDepts(request, dept);
    }

    @Operation(summary = "新增部门")
    @Log("新增部门")
    @PostMapping
    @SaCheckPermission("dept:add")
    public void addDept(@Valid @RequestBody Dept dept) throws BaseException {
        try {
            this.deptService.createDept(dept);
        } catch (Exception e) {
            message = "新增部门失败";
            log.error(message, e);
            throw new BaseException(message);
        }
    }

    @Operation(summary = "删除部门")
    @Log("删除部门")
    @DeleteMapping("/{deptIds}")
    @SaCheckPermission("dept:delete")
    public void deleteDepts(@NotBlank(message = "{required}") @PathVariable String deptIds) throws BaseException {
        try {
            String[] ids = deptIds.split(StringPool.COMMA);
            this.deptService.deleteDepts(ids);
        } catch (Exception e) {
            message = "删除部门失败";
            log.error(message, e);
            throw new BaseException(message);
        }
    }

    @Operation(summary = "修改部门")
    @Log("修改部门")
    @PutMapping
    @SaCheckPermission("dept:update")
    public void updateDept(@Valid Dept dept) throws BaseException {
        try {
            this.deptService.updateDept(dept);
        } catch (Exception e) {
            message = "修改部门失败";
            log.error(message, e);
            throw new BaseException(message);
        }
    }

    @Operation(summary = "导出部门Excel")
    @PostMapping("excel")
    @SaCheckPermission("dept:export")
    public void export(Dept dept, QueryRequest request, HttpServletResponse response) throws BaseException {
        try {
            List<Dept> depts = this.deptService.findDepts(dept, request);
//            ExcelKit.$Export(Dept.class, response).downXlsx(depts, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new BaseException(message);
        }
    }
}
