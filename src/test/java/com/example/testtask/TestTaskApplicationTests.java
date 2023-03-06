package com.example.testtask;


import com.example.testtask.controllers.MainController;
import com.example.testtask.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import static java.sql.Date.valueOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TestTaskApplicationTests {
    @Autowired
    private MainController controller;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getEmptyContext() throws Exception {

        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[]"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }



    @Test
    public void saveUser() throws Exception {
        User user = new User();
        user.setId(1L);
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andDo(print())
                .andExpect(status().isCreated());
    }
    @Test
    public void getNonExistUser() throws Exception {
        mockMvc.perform(get("/1"))
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    public void getExistingUser() throws Exception {
        User user = new User();
        user.setId(1L);
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        controller.create(user);

        mockMvc.perform(get("/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(userJson))
                .andReturn();
    }
    @Test
    public void UpdateUser() throws Exception{
        User user = new User();
        user.setId(1L);
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        user.setFirstname("Vlad");
        user.setLastname("Yehorov");
        user.setBirthday( valueOf("2017-11-15"));
        String updateUserJson = objectMapper.writeValueAsString(user);
        String ExpectedJson="{\"id\":1,\"firstname\":\"Vlad\",\"lastname\":\"Yehorov\",\"birthday\":\"2017-11-15\"}";


        controller.create(user);

        mockMvc.perform(put("/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateUserJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(ExpectedJson));
    }
    @Test
    public void DeleteUser()throws Exception{
        User user = new User();
        user.setId(1L);
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);



        controller.create(user);

        mockMvc.perform(delete("/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[]"))
                .andReturn();

        mockMvc.perform(get("/1"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andReturn();

    }



}






