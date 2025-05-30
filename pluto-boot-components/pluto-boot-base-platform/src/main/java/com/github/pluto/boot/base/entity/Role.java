package com.github.pluto.boot.base.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.pluto.boot.base.utils.TimeConverter;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * @author     ：L1nker4
 * @date       ： 创建于  2020/1/18 12:15
 * @description： 角色实体类
 */
@Data
@Table("sys_role")
@Excel("角色信息表")
public class Role implements Serializable {

    @Serial
    private static final long serialVersionUID = -1714476694755654924L;

    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;

    @NotBlank(message = "{required}")
    @Size(max = 10, message = "{noMoreThan}")
    @ExcelField(value = "角色名称")
    private String roleName;

    @Size(max = 50, message = "{noMoreThan}")
    @ExcelField(value = "角色描述")
    private String remark;

    @ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
    private Date createTime;

    @ExcelField(value = "修改时间", writeConverter = TimeConverter.class)
    private Date updateTime;

    private transient String createTimeFrom;
    private transient String createTimeTo;
    private transient String menuId;

}