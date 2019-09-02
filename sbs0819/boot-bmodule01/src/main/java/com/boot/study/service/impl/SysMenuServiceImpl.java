package com.boot.study.service.impl;

import com.boot.study.common.PageResult;
import com.boot.study.common.TreeDto;
import com.boot.study.mapper.SysMenuMapper;
import com.boot.study.model.SysMenu;
import com.boot.study.model.SysUser;
import com.boot.study.service.SysMenuService;
import com.boot.study.util.TreeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    }

    @Override
    public SysUser detail(Long id) {
        return null;
    }

    @Override
    public void update(SysMenu sysMenu) {

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

    private void recusive(List<TreeDto> rootList) {
        if (rootList.size() > 0) {
            rootList.forEach(treeDto -> {
                treeDto.setChildren(TreeUtil.convert(sysMenuMapper.selectByParentId(treeDto.getId())));
                recusive(treeDto.getChildren());
            });
        }
    }
}
