/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dbc.PreSelecao.rest;

import br.com.dbc.PreSelecao.entity.User;
import br.com.dbc.PreSelecao.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRestControllerTest {
    
    @Test
    public void contextLoads() {
    }

    private MockMvc restMockMvc;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private UserRestController userRestController;
    
    @Autowired
    private UserRepository userRepository;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.restMockMvc = MockMvcBuilders.standaloneSetup(userRestController)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setMessageConverters(jacksonMessageConverter).build();
        userRepository.deleteAll();
    }
    
    @Test
    @WithMockUser(username="admin.admin", 
            password = "jwtpass", 
            authorities = {"ADMIN_USER"})
    public void criarUserTest() throws Exception{
        User user = User.builder()
                            .firstName("Novo")
                            .lastName("usuario")
                            .username("novo.user")
                            .password("novopass")
                            .email("novouser@dbc.com.br")
                        .build();
        
        restMockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(user)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(user.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(user.getUsername()));
    }
    
    @Test
    @WithMockUser(username="admin.admin", 
            password = "jwtpass", 
            authorities = {"ADMIN_USER"})
    public void criarUserQueJaExisteTest() throws Exception{
        User user = User.builder()
                    .firstName("Novo")
                    .lastName("usuario")
                    .username("novo.user")
                    .password("novopass")
                    .email("novouser@dbc.com.br")
                .build();

        restMockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(user)));
        
        restMockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    
    @Test(expected = Exception.class)
    @WithMockUser(username="john.doe", 
            password = "jwtpass", 
            authorities = {"STANDARD_USER"})
    public void criarUserSemSerAdminExisteTest() throws Exception{
        User user = User.builder()
                    .firstName("Novo")
                    .lastName("usuario")
                    .username("novo.user")
                    .password("novopass")
                    .email("novouser@dbc.com.br")
                .build();
        
        restMockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    
    @Test
    @WithMockUser(username="admin.admin", 
            password = "jwtpass", 
            authorities = {"ADMIN_USER"})
    public void trocarSenhaDeUserTest() throws Exception{
        User user = User.builder()
                    .firstName("Novo")
                    .lastName("usuario")
                    .username("novo.user")
                    .password("novopass")
                    .email("novouser@dbc.com.br")
                .build();
        
        restMockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(user)))
                .andExpect(MockMvcResultMatchers.status().isOk());
        
        user.setPassword("novo");
        
        restMockMvc.perform(MockMvcRequestBuilders.post("/api/user/password")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(user)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(user.getUsername()));
        
    }
    
    @Test
    @WithMockUser(username="admin.admin", 
            password = "jwtpass", 
            authorities = {"ADMIN_USER"})
    public void trocarSenhaUserQueNaoExisteTest() throws Exception{
        User user = User.builder()
                    .firstName("Novo")
                    .lastName("usuario")
                    .username("novo.user")
                    .password("novopass")
                    .email("novouser@dbc.com.br")
                .build();
        
        restMockMvc.perform(MockMvcRequestBuilders.post("/api/user/password")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(user)));
        
        user.setUsername("random");
        
        restMockMvc.perform(MockMvcRequestBuilders.post("/api/user/password")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    
    @Test(expected = Exception.class)
    @WithMockUser(username="john.doe", 
            password = "jwtpass", 
            authorities = {"STANDARD_USER"})
    public void userSemSerAdminTrocaSenhaTest() throws Exception{
        User user = User.builder()
                    .firstName("Novo")
                    .lastName("usuario")
                    .username("novo.user")
                    .password("novopass")
                    .email("novouser@dbc.com.br")
                .build();
        
        restMockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    
    @Test
    @WithMockUser(username="admin.admin", 
            password = "jwtpass", 
            authorities = {"ADMIN_USER"})
    public void userAdminVerificaSeEhAdmimTest() throws Exception{
        
        restMockMvc.perform(MockMvcRequestBuilders.get("/api/user/role")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    @Test(expected = Exception.class)
    @WithMockUser(username="john.doe", 
            password = "jwtpass", 
            authorities = {"STANDARD_USER"})
    public void userSemSerAdminVerificaSeEhAdmimTest() throws Exception{
        
        restMockMvc.perform(MockMvcRequestBuilders.get("/api/user/role")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
