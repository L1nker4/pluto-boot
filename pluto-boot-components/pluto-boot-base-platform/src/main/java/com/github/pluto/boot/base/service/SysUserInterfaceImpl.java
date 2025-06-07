package com.github.pluto.boot.base.service;

import cn.dev33.satoken.stp.StpInterface;
import com.github.pluto.boot.base.common.BaseSystemConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SysUserInterfaceImpl implements StpInterface {

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        if (StringUtils.equals(loginType, BaseSystemConstant.SYS_USER)) {
            
        }
        return List.of();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return List.of();
    }
}
