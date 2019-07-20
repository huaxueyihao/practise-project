package com.gardenia.gmall.user.service.impl;

import com.gardenia.gmall.api.bean.UmsMember;
import com.gardenia.gmall.api.service.UserService;
import com.gardenia.gmall.user.mapper.UmsMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UmsMemberMapper umsMemberMapper;

    @Override
    public List<UmsMember> getAllUser() {
        return umsMemberMapper.selectAll();
    }
}
