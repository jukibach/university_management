package fpt.com.universitymanagement.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

import fpt.com.universitymanagement.entity.Account;
import fpt.com.universitymanagement.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(value = AccountController.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
class AccountControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private AccountService service;
    
    @Test
    @WithMockUser(username="spring")
    void testGetAllAccounts() throws Exception {
//        Account account1 = new Account(); // Assuming Account is a valid class
//        Account account2 = new Account();
//
//        // ... Initialize account objects
//        List<Account> accounts = Arrays.asList(account1, account2);
//
//        when(service.getAllAccounts()).thenReturn(accounts);
//
//        this.mockMvc.perform(get("/v1/api/accounts"))
//                .andExpect(status().isOk());
//        // ... Additional assertions like checking the returned content
    }
}
