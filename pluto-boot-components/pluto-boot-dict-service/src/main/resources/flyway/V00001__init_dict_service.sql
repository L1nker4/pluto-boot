CREATE TABLE `tb_dict_info` (
                                `id` BIGINT NOT NULL COMMENT '字典ID',
                                `dict_code` VARCHAR(100) NOT NULL COMMENT '字典code',
                                `dict_name` VARCHAR(255) DEFAULT NULL COMMENT '字典名称',
                                `desc` TEXT COMMENT '字典描述',
                                `status` INT DEFAULT NULL COMMENT '字典启用状态',
                                `create_ts` BIGINT DEFAULT NULL COMMENT '创建时间戳',
                                `update_ts` BIGINT DEFAULT NULL COMMENT '更新时间戳',
                                PRIMARY KEY (`id`),
                                UNIQUE KEY `uk_dict_code` (`dict_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典信息表';


CREATE TABLE `tb_dict_item` (
                                `id` BIGINT NOT NULL COMMENT 'item id',
                                `dict_id` BIGINT NOT NULL COMMENT 'dict id',
                                `dict_code` VARCHAR(100) NOT NULL COMMENT '字典code',
                                `item_code` VARCHAR(100) NOT NULL COMMENT '字典项code',
                                `item_name` VARCHAR(255) DEFAULT NULL COMMENT '字典项名称',
                                `level` INT DEFAULT 0 COMMENT '字典项层级，默认0',
                                `is_child` TINYINT(1) DEFAULT 0 COMMENT '是否为子级',
                                `parent_id` BIGINT DEFAULT NULL COMMENT '父级id，可为空',
                                `sort` INT DEFAULT 0 COMMENT '排序字段',
                                PRIMARY KEY (`id`),
                                KEY `idx_dict_id` (`dict_id`),
                                KEY `idx_dict_code` (`dict_code`),
                                KEY `idx_item_code` (`item_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典项信息表';

