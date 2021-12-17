package com.shop.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class ItemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("상품 등록 페이지 권한 테스트")
    @WithMockUser(username = "admin", roles = "ADMIN")  // admin 계정이 로그인 된 상태에서의 테스트를 위한 어노테이션
    public void itemFormAdminTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new"))
                .andDo(print())  // 콘솔창 출력용
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("상품 등록 페이지 일반 회원 접근 테스트")
    @WithMockUser(username = "user", roles = "USER")  // admin 계정이 로그인 된 상태에서의 테스트를 위한 어노테이션
    public void itemFormNotAdminTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new"))
                .andDo(print())  // 콘솔창 출력용
                .andExpect(status().isForbidden());
    }
}