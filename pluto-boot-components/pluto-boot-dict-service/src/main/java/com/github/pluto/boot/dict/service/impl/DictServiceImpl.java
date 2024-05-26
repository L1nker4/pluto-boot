package com.github.pluto.boot.dict.service.impl;

import com.github.pluto.boot.dict.entity.DictInfo;
import com.github.pluto.boot.dict.mapper.DictInfoMapper;
import com.github.pluto.boot.dict.service.DictService;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：L1nker4
 * @date ： 创建于  2024/5/26 21:58
 */
@Service
public class DictServiceImpl implements DictService {

    @Resource
    private DictInfoMapper dictInfoMapper;

    private static final String CACHE_ALL_DICT_ITEMS_KEY = "DICT_ALL_ITEMS";

    @Override
    @Cacheable(cacheNames = CACHE_ALL_DICT_ITEMS_KEY)
    public void initAllDictItems() {
        List<DictInfo> dictInfoList = dictInfoMapper.selectAll();
        System.out.println(dictInfoList);
    }
}
