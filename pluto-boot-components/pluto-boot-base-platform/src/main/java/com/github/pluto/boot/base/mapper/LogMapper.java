package com.github.pluto.boot.base.mapper;

import com.github.pluto.boot.base.entity.SysLog;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogMapper extends BaseMapper<SysLog> {
}