package com.tammy.identityservice.cotroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tammy.identityservice.dto.request.UserCreationRequest;
import com.tammy.identityservice.dto.response.UserResponse;
import com.tammy.identityservice.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;


@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserCreationRequest request;
    private UserResponse userResponse;

    // Init data for testing, common function is called before each test
    @BeforeEach
    public void initData(){
        LocalDate dob = LocalDate.of(1990, 1, 1);
        request = UserCreationRequest.builder()
                .username("tammy")
                .password("12345678")
                .firstName("Tammy")
                .lastName("Nguyen")
                .dob(dob)
                .build();
        userResponse = UserResponse.builder()
                .id("c0df4d3ace19")
                .username("tammy")
                .firstName("Tammy")
                .lastName("Nguyen")
                .dob(dob)
                .build();
    }

    @Test
    void createUserWithValidRequestIsSuccessful() throws Exception {
        // Test happy case 1: create user successfully
        // GIVEN : Input data -> InitData
        // object to String
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request); //serialize request to JSON

        // Mock service return response directly without call real service
        Mockito.when(userService.createUser(ArgumentMatchers.any()))
                .thenReturn(userResponse);


        // When + THEN : Call API

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE) ////JSON format
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code")
                        .value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("result.id")// check id so path is result.id
                        .value("c0df4d3ace19")

                );
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andReturn();

        // Then :  expect Return response is success
    }

    @Test
    void createUserWithUserNameInValidRequestIsFail() throws Exception {

        // GIVEN : Input data -> InitData
        request.setUsername("ta");
        // object to String
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request); //serialize request to JSON

       /*  it's not happening because th request has @Valid annotation. (A framework that validates the request before it reaches the controller)
        //Mock service return response directly without call real service
        Mockito.when(userService.createUser(ArgumentMatchers.any()))
                .thenReturn(userResponse);*/


        // When + THEN : Call API

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE) ////JSON format
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code")
                        .value(1003))
                .andExpect(MockMvcResultMatchers.jsonPath("message")// check id so path is result.id
                        .value("Username must be at least 3 characters")

                );
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andReturn();


    }



}
