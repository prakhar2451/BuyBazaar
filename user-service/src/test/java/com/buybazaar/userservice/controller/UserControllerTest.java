//package com.buybazaar.userservice.controller;
//
//import com.buybazaar.userservice.dto.UserDTO;
//import com.buybazaar.userservice.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import static org.mockito.BDDMockito.given;
//
//
//
//@WebMvcTest(UserController.class)
//public class UserControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Mock
//    @Autowired
//    private UserService userService;
//
//    @InjectMocks
//    private UserController userController;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
//    }
//
//    @Test
//    @WithMockUser
//    public void testCreateUser() {
//
//        UserDTO userDTO = new UserDTO();
//        userDTO.setUsername("prakhar2451");
//        userDTO.setPassword("password");
//        userDTO.setEmail("prakhar@gmail.com");
//        userDTO.setFullname("Prakhar Kumar");
//
//        given(userService.createUser(userDTO)).willReturn(userDTO);
//
//    }
//}
