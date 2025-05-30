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
 * @date       ： 创建于  2020/1/18 12:11
 * @description： 部门实体类
 */
@Data
@Table("sys_dept")
@Excel("部门信息表")
public class Dept implements Serializable {

    @Serial
    private static final long serialVersionUID = -7790334862410409053L;

    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long deptId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;

    @NotBlank(message = "{required}")
    @Size(max = 20, message = "{noMoreThan}")
    @ExcelField(value = "部门名称")
    private String deptName;

    private Double sort;

    @ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
    private Date createTime;

    @ExcelField(value = "修改时间", writeConverter = TimeConverter.class)
    private Date updateTime;

    private transient String createTimeFrom;

    private transient String createTimeTo;

}