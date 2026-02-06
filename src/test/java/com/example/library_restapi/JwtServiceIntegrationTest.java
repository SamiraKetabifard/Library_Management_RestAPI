package com.example.library_restapi;

import com.example.library_restapi.jwt.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
        "jwt.secretKey=mySecretKeyhvjvjvjvdkmldjotyolfgl1234567890",
        "jwt.expiration=86400000"
})
class JwtServiceIntegrationTest {

    @Autowired
    private JwtService jwtService;

    @Test
    void testTokenGenerationAndValidation() {
        // Arrange
        UserDetails userDetails = User.builder()
                .username("User")
                .password("password")
                .authorities(Collections.emptyList())
                .build();
        // Act - Generate token
        String token = jwtService.generateToken(userDetails);
        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        // Verify extraction
        String username = jwtService.extractUsername(token);
        assertEquals("User", username);
        // Verify validation
        boolean isValid = jwtService.isTokenValid(token, userDetails);
        assertTrue(isValid);
    }
}