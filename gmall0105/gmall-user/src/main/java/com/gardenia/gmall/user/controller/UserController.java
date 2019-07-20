package com.gardenia.gmall.user.controller;

import com.gardenia.gmall.api.bean.UmsMember;
import com.gardenia.gmall.api.bean.UmsMemberReceiveAddress;
import com.gardenia.gmall.api.service.UmsMemberReceiveAddressService;
import com.gardenia.gmall.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UmsMemberReceiveAddressService umsMemberReceiveAddressService;

    @RequestMapping("getAllUser")
    @ResponseBody
    public List<UmsMember> getAllUser(){

        List<UmsMember> umsMembers = userService.getAllUser();

        return umsMembers;
    }

    @GetMapping("getReceiveAddressByMemberId/{memberId}")
    @ResponseBody
    public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(@PathVariable String memberId){

        List<UmsMemberReceiveAddress> addressList = umsMemberReceiveAddressService.getReceiveAddressByMemberId(memberId);

        return addressList;
    }

    @RequestMapping("index")
    @ResponseBody
    public String index(){
        return "hello user";
    }


}
