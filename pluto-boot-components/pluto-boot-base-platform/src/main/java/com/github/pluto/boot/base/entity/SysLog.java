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
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author     ：L1nker4
 * @date       ： 创建于  2020/1/18 12:22
 * @description： 日志实体类
 */
@Data
@Table("sys_log")
@Excel("系统日志表")
public class SysLog implements Serializable {

    private static final long serialVersionUID = -8878596941954995444L;

    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ExcelField(value = "操作人")
    private String username;

    @ExcelField(value = "操作描述")
    private String operation;

    @ExcelField(value = "耗时（毫秒）")
    private Long time;

    @ExcelField(value = "执行方法")
    private String method;

    @ExcelField(value = "方法参数")
    private String params;

    @ExcelField(value = "IP地址")
    private String ip;

    @ExcelField(value = "操作时间", writeConverter = TimeConverter.class)
    private Date createTime;

    private transient String createTimeFrom;
    private transient String createTimeTo;

    @ExcelField(value = "操作地点")
    private String location;

}