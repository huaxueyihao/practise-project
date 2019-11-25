package com.boot.study.controller;

import com.boot.study.common.PageParam;
import com.boot.study.model.SysUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }


    @Test
    public void pageListTest() throws Exception {

        PageParam<SysUser> param = new PageParam<>();
        param.setLimit(10);
        param.setPage(1);
        String content = objectMapper.writeValueAsString(param);

        String result = mockMvc.perform(post("/user/pageList")
                .content(content)
//                .param("condition", null)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);

    }


    @Test
    public void testAdd() throws Exception{
        SysUser sysUser = new SysUser();
        sysUser.setId(4L);
        sysUser.setUserName("jerrry");
        sysUser.setPassword("123456");
        String content = objectMapper.writeValueAsString(sysUser);
        String result = mockMvc.perform(put("/user").content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }


    @Test
    public void testGet() throws Exception{
        String result = mockMvc.perform(get("/user/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }



    @Test
    public void test(){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // $2a$10$OoBxN/jVr/JioS2OrCLwRudT2N8Qq6ao44izpBn6FUWB/F5Gxqz3a
        String encode = passwordEncoder.encode("123456");
        System.out.println(encode);


    }
}
