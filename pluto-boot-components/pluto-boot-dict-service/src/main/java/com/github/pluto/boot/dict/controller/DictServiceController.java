package com.github.pluto.boot.dict.controller;

import com.github.pluto.boot.dict.entity.DictInfo;
import com.github.pluto.boot.dict.service.DictService;
import com.github.pluto.boot.web.entity.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("dict")
@Tag(name = "字典管理")
@RequiredArgsConstructor
public class DictServiceController {

    private final DictService dictService;

    @GetMapping("all_dict_info")
    @Operation(summary = "查询全部字典信息")
    public CommonResult<List<DictInfo>> allDictInfo() {
        return new CommonResult<List<DictInfo>>()
                .success(dictService.getAllDicts());
    }

}
