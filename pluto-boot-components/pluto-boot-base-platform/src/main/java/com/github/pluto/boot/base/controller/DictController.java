package com.github.pluto.boot.base.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.github.pluto.boot.base.common.QueryRequest;
import com.github.pluto.boot.base.entity.Dict;
import com.github.pluto.boot.base.exception.BaseException;
import com.github.pluto.boot.base.logging.Log;
import com.github.pluto.boot.base.service.DictService;
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


/**
 * @author     ：L1nker4
 * @date       ： 创建于  2020/1/20 19:13
 * @description： 
 */
@Slf4j
@Validated
@RestController
@RequestMapping("dict")
public class DictController extends BaseController {

    private String message;

    @Autowired
    private DictService dictService;

    @GetMapping
    @SaCheckPermission("dict:view")
    public Map<String, Object> DictList(QueryRequest request, Dict dict) {
        return getDataTable(this.dictService.findDicts(request, dict));
    }

    @Log("新增字典")
    @PostMapping
    @SaCheckPermission("dict:add")
    public void addDict(@Valid Dict dict) {
        try {
            this.dictService.createDict(dict);
        } catch (Exception e) {
            message = "新增字典成功";
            log.error(message, e);
            throw new BaseException(message);
        }
    }

    @Log("删除字典")
    @DeleteMapping("/{dictIds}")
    @SaCheckPermission("dict:delete")
    public void deleteDicts(@NotBlank(message = "{required}") @PathVariable String dictIds) throws BaseException {
        try {
            String[] ids = dictIds.split(StringPool.COMMA);
            this.dictService.deleteDicts(ids);
        } catch (Exception e) {
            message = "删除字典成功";
            log.error(message, e);
            throw new BaseException(message);
        }
    }

    @Log("修改字典")
    @PutMapping
    @SaCheckPermission("dict:update")
    public void updateDict(@Valid Dict dict) throws BaseException {
        try {
            this.dictService.updateDict(dict);
        } catch (Exception e) {
            message = "修改字典成功";
            log.error(message, e);
            throw new BaseException(message);
        }
    }

    @PostMapping("excel")
    @SaCheckPermission("dict:export")
    public void export(QueryRequest request, Dict dict, HttpServletResponse response) throws BaseException {
        try {
            List<Dict> dicts = this.dictService.findDicts(request, dict).getRecords();
//            ExcelKit.$Export(Dict.class, response).downXlsx(dicts, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new BaseException(message);
        }
    }
}
