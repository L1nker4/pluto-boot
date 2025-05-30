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
 * @description： 菜单信息实体类
 */
@Data
@Table("sys_menu")
@Excel("菜单信息表")
public class Menu implements Serializable {

    @Serial
    private static final long serialVersionUID = 7187628714679791771L;

    public static final String TYPE_MENU = "0";

    public static final String TYPE_BUTTON = "1";

    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long menuId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;

    @NotBlank(message = "{required}")
    @Size(max = 10, message = "{noMoreThan}")
    @ExcelField(value = "名称")
    private String menuName;

    @Size(max = 50, message = "{noMoreThan}")
    @ExcelField(value = "地址")
    private String path;

    @Size(max = 100, message = "{noMoreThan}")
    @ExcelField(value = "对应Vue组件")
    private String component;

    @Size(max = 50, message = "{noMoreThan}")
    @ExcelField(value = "权限")
    private String perms;

    @ExcelField(value = "图标")
    private String icon;

    @NotBlank(message = "{required}")
    @ExcelField(value = "类型", writeConverterExp = "0=按钮,1=菜单")
    private String type;

    private Double sort;

    @ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
    private Date createTime;

    @ExcelField(value = "修改时间", writeConverter = TimeConverter.class)
    private Date updateTime;

    private transient String createTimeFrom;

    private transient String createTimeTo;

}