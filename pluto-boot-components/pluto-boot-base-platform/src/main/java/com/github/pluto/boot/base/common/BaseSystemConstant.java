package com.github.pluto.boot.base.common;

/**
 * @author     ：L1nker4
 * @date       ： 创建于  2020/1/18 12:30
 * @description： 常量
 */
public class BaseSystemConstant {

    // user缓存前缀
    public static final String USER_CACHE_PREFIX = "forest:cache:user:";
    // user角色缓存前缀
    public static final String USER_ROLE_CACHE_PREFIX = "forest:cache:user:role:";
    // user权限缓存前缀
    public static final String USER_PERMISSION_CACHE_PREFIX = "forest:cache:user:permission:";
    // user个性化配置前缀
    public static final String USER_CONFIG_CACHE_PREFIX = "forest:cache:user:config:";
    // token缓存前缀
    public static final String TOKEN_CACHE_PREFIX = "forest:cache:token:";

    // 存储在线用户的 zset前缀
    public static final String ACTIVE_USERS_ZSET_PREFIX = "forest:user:active";

    // 排序规则： descend 降序
    public static final String ORDER_DESC = "descend";
    // 排序规则： ascend 升序
    public static final String ORDER_ASC = "ascend";

    // 按钮
    public static final String TYPE_BUTTON = "1";
    // 菜单
    public static final String TYPE_MENU = "0";

    //验证码缓存前缀
    public static final String CODE_PREFIX = "auth_key";

    public static final String SYS_USER = "sys-user";


}
