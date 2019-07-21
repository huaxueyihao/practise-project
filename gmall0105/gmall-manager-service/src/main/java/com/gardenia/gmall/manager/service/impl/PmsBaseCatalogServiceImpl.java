package com.gardenia.gmall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.gardenia.gmall.api.bean.PmsBaseCatalogOne;
import com.gardenia.gmall.api.bean.PmsBaseCatalogThree;
import com.gardenia.gmall.api.bean.PmsBaseCatalogTwo;
import com.gardenia.gmall.api.service.PmsBaseCatalogService;
import com.gardenia.gmall.manager.mapper.PmsBaseCatalogOneMapper;
import com.gardenia.gmall.manager.mapper.PmsBaseCatalogThreeMapper;
import com.gardenia.gmall.manager.mapper.PmsBaseCatalogTwoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class PmsBaseCatalogServiceImpl implements PmsBaseCatalogService {

    @Autowired
    private PmsBaseCatalogOneMapper pmsBaseCatalogOneMapper;

    @Autowired
    private PmsBaseCatalogTwoMapper pmsBaseCatalogTwoMapper;

    @Autowired
    private PmsBaseCatalogThreeMapper pmsBaseCatalogThreeMapper;


    @Override
    public List<PmsBaseCatalogOne> getAllCatalogOne() {

        return pmsBaseCatalogOneMapper.selectAll();
    }

    @Override
    public List<PmsBaseCatalogTwo> getCatalogTwoByParentId(Long parentId) {
        Example example = new Example(PmsBaseCatalogTwo.class);
        example.createCriteria().andEqualTo("parentId",parentId);
        return pmsBaseCatalogTwoMapper.selectByExample(example);
    }

    @Override
    public List<PmsBaseCatalogThree> getCatalogThreeByParentId(Long parentId) {
        Example example = new Example(PmsBaseCatalogThree.class);
        example.createCriteria().andEqualTo("parentId",parentId);
        return pmsBaseCatalogThreeMapper.selectByExample(example);
    }
}
