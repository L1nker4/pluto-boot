package com.github.pluto.boot.base.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.pluto.boot.base.common.RegexpConstant;
import com.github.pluto.boot.base.utils.TimeConverter;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author     ：L1nker4
 * @date       ： 创建于  2020/1/18 12:11
 * @description： 用户实体类
 */
@Data
@Table("sys_user")
@Excel("用户信息表")
@ToString
public class SysUser implements Serializable {

    @Serial
    private static final long serialVersionUID = -4852732617765810959L;
    /**
     * 账户状态
     */
    public static final String STATUS_VALID = "1";

    public static final String STATUS_LOCK = "0";

    public static final String DEFAULT_AVATAR = "default.jpg";

    /**
     * 性别
     */
    public static final String SEX_MALE = "0";

    public static final String SEX_FEMALE = "1";

    public static final String SEX_UNKNOW = "2";

    /**
     * 默认密码
     */
    public static final String DEFAULT_PASSWORD = "123456";

    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @Size(min = 4, max = 10, message = "{range}")
    @ExcelField(value = "用户名")
    private String username;

    private String password;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long deptId;

    @ExcelField(value = "部门")
    private transient String deptName;

    @Size(max = 50, message = "{noMoreThan}")
    @Email(message = "{email}")
    @ExcelField(value = "邮箱")
    private String email;

    @Pattern(regexp = RegexpConstant.MOBILE_REG, message = "{mobile}")
    @ExcelField(value = "手机号")
    private String mobile;

    @NotBlank(message = "{required}")
    @ExcelField(value = "状态", writeConverterExp = "0=锁定,1=有效")
    private String status;

    @ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
    private Date createTime;

    private Date updateTime;

    @ExcelField(value = "最后登录时间", writeConverter = TimeConverter.class)
    private Date lastLoginTime;

    @NotBlank(message = "{required}")
    @ExcelField(value = "性别", writeConverterExp = "0=男,1=女,2=保密")
    private String sex;

    @Size(max = 100, message = "{noMoreThan}")
    @ExcelField(value = "个人描述")
    private String description;

    private String avatar;

    @NotBlank(message = "{required}")
    private transient String roleId;
    @ExcelField(value = "角色")
    private transient String roleName;

    // 排序字段
    private transient String sortField;

    // 排序规则 ascend 升序 descend 降序
    private transient String sortOrder;

    private transient String createTimeFrom;
    private transient String createTimeTo;

    private transient String id;

    /**
     * shiro-redis v3.1.0 必须要有 getAuthCacheKey()或者 getId()方法
     * # Principal id field name. The field which you can get unique id to identify this principal.
     * # For example, if you use UserInfo as Principal class, the id field maybe userId, userName, email, etc.
     * # Remember to add getter to this id field. For example, getUserId(), getUserName(), getEmail(), etc.
     * # Default value is authCacheKey or id, that means your principal object has a method called "getAuthCacheKey()" or "getId()"
     *
     * @return userId as Principal id field name
     */
    @JsonSerialize(using = ToStringSerializer.class)
    public Long getAuthCacheKey() {
        return userId;
    }
}
