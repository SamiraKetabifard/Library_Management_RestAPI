package com.example.library_restapi.controller;

import com.example.library_restapi.dto.RatingDto;
import com.example.library_restapi.service.BookRatingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookRatingController.class)
//@AutoConfigureMockMvc(addFilters = false)
class BookRatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookRatingService bookRatingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "USER")
    void rateBook_ValidRating_Success() throws Exception {
        RatingDto ratingDto = new RatingDto(5, 1L);

        when(bookRatingService.rateBook(any(RatingDto.class), anyString()))
                .thenReturn("Book rated successfully");

        mockMvc.perform(post("/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ratingDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Book rated successfully"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void rateBook_InvalidRating_BadRequest() throws Exception {
        RatingDto ratingDto = new RatingDto(6, 1L); // Invalid rating (max is 5)

        mockMvc.perform(post("/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ratingDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    void rateBook_NotBorrowed_BadRequest() throws Exception {
        RatingDto ratingDto = new RatingDto(5, 1L);

        when(bookRatingService.rateBook(any(RatingDto.class), anyString()))
                .thenThrow(new RuntimeException("You haven't borrowed this book"));

        mockMvc.perform(post("/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ratingDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("You haven't borrowed this book"));
    }

}