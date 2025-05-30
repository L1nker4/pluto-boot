package com.github.pluto.boot.base.mapper;

import com.github.pluto.boot.base.entity.SysUser;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.paginate.Page;
import org.apache.ibatis.annotations.Param;

public interface SysUserMapper extends BaseMapper<SysUser> {

    Page<SysUser> findUserDetail(Page page, @Param("user") SysUser sysUser);

    /**
     * 获取单个用户详情
     *
     * @param username 用户名
     * @return 用户信息
     */
    SysUser findDetail(String username);
}