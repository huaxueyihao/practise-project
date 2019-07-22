package com.gardenia.gmall.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gardenia.gmall.api.bean.PmsBaseAttrInfo;
import com.gardenia.gmall.api.bean.PmsBaseAttrValue;
import com.gardenia.gmall.api.service.PmsBaseAttrService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class BaseAttrController {


    @Reference
    private PmsBaseAttrService pmsBaseAttrService;

    @GetMapping("getBaseAttrInfoList/{catalogId}")
    @ResponseBody
    public List<PmsBaseAttrInfo> getBaseAttrInfoList(@PathVariable Long catalogId){
        return pmsBaseAttrService.getBaseAttrInfoList(catalogId);
    }


    @GetMapping("getBaseAttrInfoList/{attrNameId}/{spuId}")
    @ResponseBody
    public PmsBaseAttrValue getSpuAttr(@PathVariable Long attrNameId, @PathVariable Long spuId){

        return pmsBaseAttrService.getBaseAttrValue(attrNameId,spuId);
    }

}
