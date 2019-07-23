package com.gardenia.gmall.manager.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@CrossOrigin
public class SpuController {



    @GetMapping("spuList/{catalogId}")
    @ResponseBody
    public List spuList(@PathVariable String catalogId){


        return null;
    }



}
