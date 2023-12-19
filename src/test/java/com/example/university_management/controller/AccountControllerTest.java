package com.example.university_management.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

import com.example.university_management.entity.Account;
import com.example.university_management.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(AccountController.class)
class AccountControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private AccountService service;
    
    @Test
    void testHelloWorld() throws Exception {
        this.mockMvc.perform(get("/v1/api/accounts/hello-world"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello"));
    }
    
    @Test
    void testGetAllAccounts() throws Exception {
        Account account1 = new Account(); // Assuming Account is a valid class
        Account account2 = new Account();
        Account account3 = new Account();
        
        // ... Initialize account objects
        List<Account> accounts = Arrays.asList(account1, account2);
        
        when(service.getAllAccounts()).thenReturn(accounts);
        
        this.mockMvc.perform(get("/v1/api/accounts"))
                .andExpect(status().isOk());
        // ... Additional assertions like checking the returned content
    }
}
