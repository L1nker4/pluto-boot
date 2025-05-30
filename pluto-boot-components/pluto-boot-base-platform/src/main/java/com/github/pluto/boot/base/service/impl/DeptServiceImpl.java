package com.github.pluto.boot.base.service.impl;



import com.github.pluto.boot.base.common.BaseSystemConstant;
import com.github.pluto.boot.base.common.QueryRequest;
import com.github.pluto.boot.base.common.Tree;
import com.github.pluto.boot.base.entity.Dept;
import com.github.pluto.boot.base.mapper.DeptMapper;
import com.github.pluto.boot.base.service.DeptService;
import com.github.pluto.boot.base.utils.SortUtil;
import com.github.pluto.boot.base.utils.TreeUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.github.pluto.boot.base.entity.table.DeptTableDef.DEPT;

@Slf4j
@Service("deptService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

    @Resource
    private DeptMapper deptMapper;

    @Override
    public Map<String, Object> findDepts(QueryRequest request, Dept dept) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Dept> depts = findDepts(dept, request);
            List<Tree<Dept>> trees = new ArrayList<>();
            buildTrees(trees, depts);
            Tree<Dept> deptTree = TreeUtil.build(trees);

            result.put("rows", deptTree);
            result.put("total", depts.size());
        } catch (Exception e) {
            log.error("获取部门列表失败", e);
            result.put("rows", null);
            result.put("total", 0);
        }
        return result;
    }

    @Override
    public List<Dept> findDepts(Dept dept, QueryRequest request) {

        QueryWrapper queryWrapper = QueryWrapper.create()
                .select().from(DEPT)
                .where(DEPT.DEPT_NAME.eq(dept.getDeptName())
                        .when(StringUtils.isNotBlank(dept.getDeptName())))
                .and(DEPT.CREATE_TIME.between(dept.getCreateTimeFrom(), dept.getCreateTimeTo())
                        .when(StringUtils.isNotBlank(dept.getCreateTimeFrom()) && StringUtils.isNotBlank(dept.getCreateTimeTo())));

        SortUtil.handleWrapperSort(request, queryWrapper, "sort", BaseSystemConstant.ORDER_ASC, true);
        return deptMapper.selectListByQuery(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDept(Dept dept) {
        Long parentId = dept.getParentId();
        if (parentId == null) {
            dept.setParentId(0L);
        }
        dept.setCreateTime(new Date());
        this.save(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDept(Dept dept) {
        dept.setUpdateTime(new Date());
        this.updateById(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDepts(String[] deptIds) {
        this.delete(Arrays.asList(deptIds));
    }

    private void buildTrees(List<Tree<Dept>> trees, List<Dept> depts) {
        depts.forEach(dept -> {
            Tree<Dept> tree = new Tree<>();
            tree.setId(dept.getDeptId().toString());
            tree.setKey(tree.getId());
            tree.setParentId(dept.getParentId().toString());
            tree.setText(dept.getDeptName());
            tree.setCreateTime(dept.getCreateTime());
            tree.setUpdateTime(dept.getUpdateTime());
            tree.setOrder(dept.getSort());
            tree.setTitle(tree.getText());
            tree.setValue(tree.getId());
            trees.add(tree);
        });
    }

    private void delete(List<String> deptIds) {
        removeByIds(deptIds);

        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(DEPT.PARENT_ID.in(deptIds)
                        .when(CollectionUtils.isNotEmpty(deptIds)));

        List<Dept> depts = deptMapper.selectListByQuery(queryWrapper);

        if (CollectionUtils.isNotEmpty(depts)) {
            List<String> deptIdList = new ArrayList<>();
            depts.forEach(d -> deptIdList.add(String.valueOf(d.getDeptId())));
            this.delete(deptIdList);
        }
    }
}
