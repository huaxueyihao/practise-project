package com.gardenia.gmall.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gardenia.gmall.api.bean.PmsBaseCatalogOne;
import com.gardenia.gmall.api.bean.PmsBaseCatalogThree;
import com.gardenia.gmall.api.bean.PmsBaseCatalogTwo;
import com.gardenia.gmall.api.service.PmsBaseCatalogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CatalogController {


    @Reference
    private PmsBaseCatalogService pmsBaseCatalogService;


    @RequestMapping("CatalogOneList")
    @ResponseBody
    public List<PmsBaseCatalogOne> getCatalogOneList(){
        return pmsBaseCatalogService.getAllCatalogOne();
    }


    @GetMapping("CatalogTwoList/{parentId}")
    @ResponseBody
    public List<PmsBaseCatalogTwo> getCatalogTwoList(@PathVariable Long parentId){
        return pmsBaseCatalogService.getCatalogTwoByParentId(parentId);
    }


    @GetMapping("CatalogThreeList/{parentId}")
    @ResponseBody
    public List<PmsBaseCatalogThree> getCatalogThreeList(@PathVariable Long parentId){
        return pmsBaseCatalogService.getCatalogThreeByParentId(parentId);
    }

}
