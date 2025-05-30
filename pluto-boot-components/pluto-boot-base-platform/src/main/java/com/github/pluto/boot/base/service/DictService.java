package com.github.pluto.boot.base.service;


import com.github.pluto.boot.base.common.QueryRequest;
import com.github.pluto.boot.base.entity.Dict;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

public interface DictService extends IService<Dict> {

    Page<Dict> findDicts(QueryRequest request, Dict dict);

    void createDict(Dict dict);

    void updateDict(Dict dicdt);

    void deleteDicts(String[] dictIds);

}
