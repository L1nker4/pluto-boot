package com.github.pluto.boot.dict.service;

import com.github.pluto.boot.dict.entity.DictInfo;
import com.github.pluto.boot.dict.entity.DictItem;

import java.util.List;

public interface DictService {

    List<DictInfo> initAllDictItems();

    DictItem getDictItemByCode(String dictCode, String itemCode);

    DictInfo getDictInfoByDictCode(String dictCode);
}
