package com.gardenia.gmall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.gardenia.gmall.api.bean.PmsBaseAttrInfo;
import com.gardenia.gmall.api.bean.PmsBaseAttrValue;
import com.gardenia.gmall.api.service.PmsBaseAttrService;
import com.gardenia.gmall.manager.mapper.PmsBaseAttrInfoMapper;
import com.gardenia.gmall.manager.mapper.PmsBaseAttrValueMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Service
public class PmsBaseAttrServiceImpl implements PmsBaseAttrService {

    @Autowired
    private PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;

    @Autowired
    private PmsBaseAttrValueMapper pmsBaseAttrValueMapper;



    @Override
    public List<PmsBaseAttrInfo> getBaseAttrInfoList(Long catalogId) {
        return null;
    }

    @Override
    public PmsBaseAttrValue getBaseAttrValue(Long attrNameId, Long productId) {
        return null;
    }

    @Override
    public int saveAttrValue(PmsBaseAttrInfo pmsBaseAttrInfo) {
        int count = 0;
        List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
        if(CollectionUtils.isNotEmpty(attrValueList)){
            for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                pmsBaseAttrValue.setAttrNameId(pmsBaseAttrInfo.getId());
                count += pmsBaseAttrValueMapper.insertSelective(pmsBaseAttrValue);
            }

        }
        return count;
    }

    @Override
    public int saveAttrInfo( PmsBaseAttrInfo pmsBaseAttrInfo) {
        return pmsBaseAttrInfoMapper.insertSelective(pmsBaseAttrInfo);
    }
}
