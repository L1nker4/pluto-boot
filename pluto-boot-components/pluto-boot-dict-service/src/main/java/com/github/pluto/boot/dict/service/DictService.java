package com.github.pluto.boot.dict.service;

import com.github.pluto.boot.dict.entity.DictItem;

public interface DictService {

    void initAllDictItems();

    DictItem getDictItemByCode(String dictCode, String itemCode);

    DictInfo getDictInfoByDictCode(String dictCode);
}
