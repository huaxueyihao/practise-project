package com.boot.study.service.impl;

import com.boot.study.common.PageResult;
import com.boot.study.common.TreeDto;
import com.boot.study.common.ZTreeDto;
import com.boot.study.mapper.SysMenuMapper;
import com.boot.study.model.SysMenu;
import com.boot.study.service.SysMenuService;
import com.boot.study.util.TreeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public PageResult<SysMenu> pageList(Integer page, Integer limit, String menuName) {
        PageResult<SysMenu> pageResult = new PageResult<>();
        Example example = new Example(SysMenu.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(menuName)) {
            criteria.andLike("menuName", "%'" + menuName + "'%");
        }
        int count = sysMenuMapper.selectCountByExample(example);
        if (count > 0) {
            int offset = (page - 1) * limit;
            RowBounds rowBounds = new RowBounds(offset, limit);
            List<SysMenu> sysMenus = sysMenuMapper.selectByExampleAndRowBounds(example, rowBounds);
            pageResult.setDataList(sysMenus);
            pageResult.setCount(count);
        }
        return pageResult;
    }

    @Override
    public void addUser(SysMenu sysMenu) {
        sysMenuMapper.insert(sysMenu);
    }

    @Override
    public SysMenu detail(Long id) {
        return sysMenuMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(SysMenu sysMenu) {
        SysMenu entity = sysMenuMapper.selectByPrimaryKey(sysMenu.getId());
        if(Objects.nonNull(entity)){
            entity.setLittleIcon(sysMenu.getLittleIcon());
            entity.setMenuName(sysMenu.getMenuName());
            entity.setParentId(sysMenu.getParentId());
            entity.setRouteUrl(sysMenu.getRouteUrl());
            sysMenuMapper.updateByPrimaryKey(entity);
        }
    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public void batchRemove(Long[] ids) {

    }

    @Override
    public List<TreeDto> menuTree(Long parentId) {
        if (parentId == null || parentId < 0) {
            parentId = 0L;
        }
        List<SysMenu> menuList = sysMenuMapper.selectByParentId(parentId);
        List<TreeDto> rootList = TreeUtil.convert(menuList);
        recusive(rootList);
        return rootList;
    }

    @Override
    public List<ZTreeDto> menuZTree(Long parentId) {
        if (parentId == null || parentId < 0) {
            parentId = 0L;
        }
        List<SysMenu> menuList = sysMenuMapper.selectByParentId(parentId);
        List<ZTreeDto> rootList = TreeUtil.convertZtree(menuList);
        recusiveZtree(rootList);
        return rootList;
    }

    private void recusiveZtree(List<ZTreeDto> rootList) {
        if (rootList.size() > 0) {
            rootList.forEach(treeDto -> {
                List<SysMenu> sysMenus = sysMenuMapper.selectByParentId(treeDto.getId());
                if(sysMenus != null && sysMenus.size() > 0){
                    treeDto.setChildren(TreeUtil.convertZtree(sysMenus));
                    treeDto.setOpen(true);
                    recusiveZtree(treeDto.getChildren());
                }else{
                    treeDto.setOpen(false);
                }

            });
        }
    }

    private void recusive(List<TreeDto> rootList) {
        if (rootList.size() > 0) {
            rootList.forEach(treeDto -> {
                treeDto.setChildren(TreeUtil.convert(sysMenuMapper.selectByParentId(treeDto.getId())));
                recusive(treeDto.getChildren());
            });
        }
    }
}
