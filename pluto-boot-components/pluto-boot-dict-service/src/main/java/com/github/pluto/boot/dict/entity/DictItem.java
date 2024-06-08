package com.github.pluto.boot.dict.entity;

import com.mybatisflex.annotation.Table;
import lombok.Data;

/**
 * @author ：L1nker4
 * @date ： 创建于  2024/5/26 20:59
 */
@Data
@Table("tb_dict_item")
public class DictItem {

    /**
     * item id
     */
    private Long id;

    /**
     * dict id
     */
    private Long dictId;

    /**
     * 字典code
     */
    private String dictCode;

    /**
     * 字典项code
     */
    private String itemCode;

    /**
     * 字典项名称
     */
    private String itemName;

    /**
     * 字典项层级，默认0
     */
    private Integer level;

    /**
     * 是否为子级
     */
    private Boolean isChild;

    /**
     * 父级id，可为空
     */
    private Long parentId;

    /**
     * 排序字段
     */
    private Integer sort;
}
