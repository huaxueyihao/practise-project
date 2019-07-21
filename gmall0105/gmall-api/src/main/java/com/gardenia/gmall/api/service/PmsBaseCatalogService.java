package com.gardenia.gmall.api.service;

import com.gardenia.gmall.api.bean.PmsBaseCatalogOne;
import com.gardenia.gmall.api.bean.PmsBaseCatalogThree;
import com.gardenia.gmall.api.bean.PmsBaseCatalogTwo;

import java.util.List;

public interface PmsBaseCatalogService {

    List<PmsBaseCatalogOne> getAllCatalogOne();

    List<PmsBaseCatalogTwo> getCatalogTwoByParentId(Long parentId);

    List<PmsBaseCatalogThree> getCatalogThreeByParentId(Long parentId);

}
