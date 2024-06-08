package com.github.pluto.boot.dict.service.impl;

import com.github.pluto.boot.dict.entity.DictInfo;
import com.github.pluto.boot.dict.entity.DictItem;
import com.github.pluto.boot.dict.entity.table.DictInfoTableDef;
import com.github.pluto.boot.dict.entity.table.DictItemTableDef;
import com.github.pluto.boot.dict.mapper.DictInfoMapper;
import com.github.pluto.boot.dict.mapper.DictItemMapper;
import com.github.pluto.boot.dict.service.DictService;
import com.mybatisflex.core.query.QueryWrapper;
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

    @Resource
    private DictItemMapper dictItemMapper;

    private static final String CACHE_ALL_DICT_ITEMS_KEY = "DICT_ALL_ITEMS";

    @Override
    @Cacheable(cacheNames = CACHE_ALL_DICT_ITEMS_KEY)
    public List<DictInfo> initAllDictItems() {
        return dictInfoMapper.selectAll();
    }

    @Override
    public DictItem getDictItemByCode(String dictCode, String itemCode) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.where(DictItemTableDef.DICT_ITEM.DICT_CODE.eq(itemCode))
                .and(DictItemTableDef.DICT_ITEM.ITEM_CODE.eq(dictCode));
        return dictItemMapper.selectOneByQuery(wrapper);
    }

    @Override
    public DictInfo getDictInfoByDictCode(String dictCode) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.where(DictInfoTableDef.DICT_INFO.DICT_CODE.eq(dictCode));
        return dictInfoMapper.selectOneByQuery(wrapper);
    }
}
