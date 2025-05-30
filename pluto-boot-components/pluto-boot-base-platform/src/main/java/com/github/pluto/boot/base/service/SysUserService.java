package com.github.pluto.boot.base.service;


import com.github.pluto.boot.base.common.QueryRequest;
import com.github.pluto.boot.base.entity.SysUser;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

public interface SysUserService extends IService<SysUser> {

    /**
     * 通过用户名查找用户
     *
     * @param username username
     * @return user
     */
    SysUser findByName(String username);

    /**
     * 查询用户详情，包括基本信息，用户角色，用户部门
     *
     * @param sysUser user
     * @param queryRequest queryRequest
     * @return IPage
     */
    Page<SysUser> findUserDetail(SysUser sysUser, QueryRequest queryRequest);

    /**
     * 更新用户登录时间
     *
     * @param username username
     */
    void updateLoginTime(String username) throws Exception;

    /**
     * 新增用户
     *
     * @param sysUser user
     */
    void createUser(SysUser sysUser) throws Exception;

    /**
     * 修改用户
     *
     * @param sysUser user
     */
    void updateUser(SysUser sysUser) throws Exception;

    /**
     * 删除用户
     *
     * @param userIds 用户 id数组
     */
    void deleteUsers(String[] userIds) throws Exception;

    /**
     * 更新个人信息
     *
     * @param sysUser 个人信息
     */
    void updateProfile(SysUser sysUser) throws Exception;

    /**
     * 更新用户头像
     *
     * @param username 用户名
     * @param avatar   用户头像
     */
    void updateAvatar(String username, String avatar) throws Exception;

    /**
     * 更新用户密码
     *
     * @param username 用户名
     * @param password 新密码
     */
    void updatePassword(String username, String password) throws Exception;

    /**
     * 注册用户
     *
     * @param username 用户名
     * @param password 密码
     */
    void regist(String username, String password) throws Exception;

    /**
     * 重置密码
     *
     * @param usernames 用户集合
     */
    void resetPassword(String[] usernames) throws Exception;

}
