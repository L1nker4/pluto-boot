package com.github.pluto.boot.dict.entity;

import lombok.Data;

/**
 * @author ：L1nker4
 * @date ： 创建于  2024/5/26 20:59
 */
@Data
public class DictItem {

    private Long id;

    private Long dictId;

    private String dictCode;

    private String code;

    private String name;

    private Integer sort;
}
