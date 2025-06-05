package com.github.pluto.boot.base.controller;


import com.github.pluto.boot.cache.entity.RedisInfo;
import com.github.pluto.boot.cache.service.RedisService;
import com.github.pluto.boot.web.entity.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("redis")
public class RedisController {

    @Autowired
    private RedisService redisService;

    @GetMapping("info")
    public CommonResult<List<RedisInfo>> getRedisInfo() {
        List<RedisInfo> infoList = this.redisService.getRedisInfo();
        return new CommonResult<List<RedisInfo>>().data(infoList);
    }

    @GetMapping("keysSize")
    public Map<String, Object> getKeysSize() {
        return redisService.getKeysSize();
    }

    @GetMapping("memoryInfo")
    public Map<String, Object> getMemoryInfo() {
        return redisService.getMemoryInfo();
    }
}
