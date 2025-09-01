package com.example.library_restapi;

import com.example.library_restapi.config.SecurityConfig;
import com.example.library_restapi.controller.AdminController;
import com.example.library_restapi.controller.AuthController;
import com.example.library_restapi.controller.BookController;
import com.example.library_restapi.entity.Book;
import com.example.library_restapi.jwt.JwtService;
import com.example.library_restapi.repository.UserRepository;
import com.example.library_restapi.service.AuthenticationService;
import com.example.library_restapi.service.BookService;
import com.example.library_restapi.service.BorrowRecordService;
import com.example.library_restapi.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({
        BookController.class,
        AuthController.class,
        AdminController.class
})
@Import(SecurityConfig.class)
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private AuthenticationService authenticationService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private BorrowRecordService borrowRecordService;

    @Test
    void authenticatedAccessToAuthEndpoints_Allowed() throws Exception {
        // For register endpoint
        mockMvc.perform(post("/auth/registeruser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"samira\",\"password\":\"12\",\"email\":\"samira@gmail.com\"}"))
                .andExpect(status().isOk());

        // For login endpoint
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"samira\",\"password\":\"12\"}"))
                .andExpect(status().isOk());
    }
    @Test
    void unauthenticatedAccessToProtectedEndpoints_Denied() throws Exception {
        mockMvc.perform(get("/books/all"))
                .andExpect(status().isForbidden());
    }
    @Test
    @WithMockUser(roles = "USER")
    void userAccessToUserEndpoints_Allowed() throws Exception {
        Book book = new Book();
        book.setTitle("AI");
        book.setAuthor("Jac");
        book.setIsbnNumber("1");
        book.setQuantity(1);
        book.setIsAvailable(true);

        when(bookService.findByAuthor("Jac"))
                .thenReturn(List.of(book));

        mockMvc.perform(get("/books/by-author")
                        .param("author", "Jac"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("AI"))
                .andExpect(jsonPath("$[0].author").value("Jac"));
    }
    @Test
    @WithMockUser(roles = "USER")
    void userAccessToAdminEndpoints_Denied() throws Exception {
        mockMvc.perform(get("/books/highly-rated"))
                .andExpect(status().isForbidden());
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void adminAccessToAdminEndpoints_Allowed() throws Exception {

        Book book = new Book();
        book.setTitle("Highly Rated Book");
        book.setAuthor("hafez");
        book.setIsbnNumber("1");
        book.setQuantity(1);
        book.setIsAvailable(true);
        //act
        when(bookService.getHighlyRatedBooks())
                .thenReturn(List.of(book));

        mockMvc.perform(get("/books/highly-rated"))
                .andExpect(status().isOk());
    }
}