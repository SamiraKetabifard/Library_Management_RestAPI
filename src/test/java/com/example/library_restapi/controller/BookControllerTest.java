package com.example.library_restapi.controller;

import com.example.library_restapi.dto.BookCategoryCountDto;
import com.example.library_restapi.dto.BookDto;
import com.example.library_restapi.entity.Book;
import com.example.library_restapi.jwt.JwtAuthFilter;
import com.example.library_restapi.jwt.JwtService;
import com.example.library_restapi.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc(addFilters = false)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void addBook_AdminRole_Success() throws Exception {
        BookDto bookDto = new BookDto("AI", "loius",
                "1", 5, true, "SCIENCE");
        Book book = new Book();
        book.setTitle("AI");

        when(bookService.addBook(any(BookDto.class))).thenReturn(book);

        mockMvc.perform(post("/books/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("AI"));
    }
    @Test
    @WithMockUser
    void getBookById_Exists_Success() throws Exception {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Existing Book");

        when(bookService.getBookById(1L)).thenReturn(book);

        mockMvc.perform(get("/books/getbookbyid/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Existing Book"));
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateBook_AdminRole_Success() throws Exception {
        BookDto bookDto = new BookDto("AI2", "mary",
                "1", 5, true, "SCIENCE");

        mockMvc.perform(put("/books/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Book with ID 1 has been updated successfully"));
    }
    @Test
    @WithMockUser(roles = "USER")
    void getBooksByAuthor_Exists_Success() throws Exception {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("AI");
        book1.setAuthor("Jac");
        book1.setIsbnNumber("1");
        book1.setQuantity(1);
        book1.setIsAvailable(true);

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("AI2");
        book2.setAuthor("Jac");
        book2.setIsbnNumber("1");
        book2.setQuantity(1);
        book2.setIsAvailable(true);

        when(bookService.findByAuthor("Jac"))
                .thenReturn(List.of(book1, book2));

        mockMvc.perform(get("/books/by-author")
                        .param("author", "Jac"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].author").value("Jac"))
                .andExpect(jsonPath("$[1].author").value("Jac"));
    }

    @Test
    @WithMockUser
    void getBookCountByCategory_Success() throws Exception {
        BookCategoryCountDto dto1 = new BookCategoryCountDto("SCIENCE", 3L);
        BookCategoryCountDto dto2 = new BookCategoryCountDto("LITERATURE", 2L);

        when(bookService.getBookCountByCategory()).thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/books/category-stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].category").value("SCIENCE"))
                .andExpect(jsonPath("$[0].count").value(3));
    }
}