package com.github.pluto.boot.base.service.impl;


import com.github.pluto.boot.base.common.QueryRequest;
import com.github.pluto.boot.base.entity.Dict;
import com.github.pluto.boot.base.mapper.DictMapper;
import com.github.pluto.boot.base.service.DictService;
import com.github.pluto.boot.base.utils.SortUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.github.pluto.boot.base.entity.table.DictTableDef.DICT;

import java.util.Arrays;
import java.util.List;


/**
 * @author     ：L1nker4
 * @date       ： 创建于  2020/1/20 19:12
 * @description：
 */
@Slf4j
@Service("dictService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Override
    public Page<Dict> findDicts(QueryRequest request, Dict dict) {
        try {
            QueryWrapper queryWrapper = QueryWrapper.create()
                    .select()
                    .where(DICT.KEY.eq(dict.getKey()).when(StringUtils.isNotBlank(dict.getKey())))
                    .and(DICT.VALUE.eq(dict.getValue()).when(StringUtils.isNotBlank(dict.getValue())))
                    .and(DICT.TABLE_NAME.eq(dict.getTableName()).when(StringUtils.isNotBlank(dict.getTableName())))
                    .and(DICT.FIELD_NAME.eq(dict.getFieldName()).when(StringUtils.isNotBlank(dict.getFieldName())));

            Page<Dict> page = new Page<>();
            SortUtil.handlePageSort(request,queryWrapper, page, true);
            return page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDict(Dict dict) {
        this.save(dict);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDict(Dict dict) {
        updateById(dict);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDicts(String[] dictIds) {
        List<String> list = Arrays.asList(dictIds);
        mapper.deleteBatchByIds(list);
    }
}
