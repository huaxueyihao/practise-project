package com.gardenia.gmall.user.service.impl;

import com.gardenia.gmall.user.mapper.UserMapper;
import com.gardenia.gmall.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
}
