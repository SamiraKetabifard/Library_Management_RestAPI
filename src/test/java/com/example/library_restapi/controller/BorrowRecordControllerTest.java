package com.example.library_restapi.controller;

import com.example.library_restapi.config.SecurityConfig;
import com.example.library_restapi.dto.BorrowRecordDto;
import com.example.library_restapi.dto.HistoryDto;
import com.example.library_restapi.entity.BorrowRecord;
import com.example.library_restapi.jwt.JwtService;
import com.example.library_restapi.repository.UserRepository;
import com.example.library_restapi.service.BorrowRecordService;
import com.example.library_restapi.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BorrowRecordController.class)
@Import(SecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class BorrowRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BorrowRecordService borrowRecordService;

    // Add these required security mocks
    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void borrowBook_Success() throws Exception {
        BorrowRecord record = new BorrowRecord();
        record.setId(1L);

        when(borrowRecordService.borrowTheBook(1L)).thenReturn(record);

        mockMvc.perform(post("/borrowRecords/borrowBook/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
    @Test
    @WithMockUser
    void returnBook_Success() throws Exception {
        BorrowRecord record = new BorrowRecord();
        record.setId(1L);
        record.setReturnDate(LocalDate.now());

        when(borrowRecordService.returnTheBook(1L)).thenReturn(record);

        mockMvc.perform(post("/borrowRecords/returnBook/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.returnDate").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllBorrowRecords_AdminRole_Success() throws Exception {
        BorrowRecordDto dto = new BorrowRecordDto(1L, "samira",
                "AI", false);

        when(borrowRecordService.getAllBorrowRecordsWithDetails()).thenReturn(List.of(dto));

        mockMvc.perform(get("/borrowRecords/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("samira"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getMyBorrowedBooks_UserRole_Success() throws Exception {
        HistoryDto dto = new HistoryDto("AI", LocalDate.now(),
                LocalDate.now().plusDays(14), false);

        when(borrowRecordService.getBorrowHistory()).thenReturn(List.of(dto));

        mockMvc.perform(get("/borrowRecords/my-borrowed-books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookTitle").value("AI"));
    }
}