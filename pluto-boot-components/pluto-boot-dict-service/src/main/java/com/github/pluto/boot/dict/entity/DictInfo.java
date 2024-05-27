package com.github.pluto.boot.dict.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.RelationOneToMany;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.Data;

import java.util.List;

/**
 * @author ：L1nker4
 * @date ： 创建于  2024/5/26 20:37
 */
@Data
@Table("tb_dict_info")
public class DictInfo {

    /**
     * 字典ID
     */
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long id;

    /**
     * 字典code
     */
    private String dictCode;

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典描述
     */
    private String desc;

    /**
     * 字典启用状态
     */
    private Integer status;
   
    private Long createTs;

    private Long updateTs;

    /**
     * 关联item信息
     */
    @RelationOneToMany(selfField = "id", targetField = "dictId")
    private List<DictItem> items;
}
