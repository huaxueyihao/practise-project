package com.gardenia.gmall.api.service;


import com.gardenia.gmall.api.bean.PmsBaseAttrInfo;
import com.gardenia.gmall.api.bean.PmsBaseAttrValue;

import java.util.List;

public interface PmsBaseAttrService {

        List<PmsBaseAttrInfo> getBaseAttrInfoList(Long catalogId);


        PmsBaseAttrValue getBaseAttrValue(Long attrNameId,Long productId);


        int saveAttrValue(PmsBaseAttrInfo pmsBaseAttrInfo);

        int saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);


}
