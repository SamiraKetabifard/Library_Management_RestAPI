package com.example.library_restapi;

import com.example.library_restapi.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import javax.crypto.SecretKey;
import java.lang.reflect.Method;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private final String SECRET_KEY = "mySecretKeyhvjvjvjvdkmldjotyolfgl1234567890";
    private final long EXPIRATION = 86400000L;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", EXPIRATION);

        userDetails = User.builder()
                .username("User")
                .password("password")
                .authorities(Collections.emptyList())
                .build();
    }
    @Test
    void testGetSignInKey() {
        // Act
        SecretKey key = jwtService.getSignInKey();
        // Assert
        assertNotNull(key);
        assertEquals("HmacSHA256", key.getAlgorithm());
    }
    @Test
    void testGenerateTokenWithUserDetails() {
        // Act
        String token = jwtService.generateToken(userDetails);
        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        // Verify token can be parsed
        String username = jwtService.extractUsername(token);
        assertEquals("User", username);
    }
    @Test
    void testGenerateTokenWithExtraClaims() {
        // Arrange
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "ADMIN");
        extraClaims.put("userId", 1);
        // Act
        String token = jwtService.generateToken(extraClaims, userDetails);
        // Assert
        assertNotNull(token);
        // Verify claims extraction
        String username = jwtService.extractUsername(token);
        assertEquals("User", username);
    }
    @Test
    void testExtractUsername() {
        // Arrange
        String token = jwtService.generateToken(userDetails);
        // Act
        String username = jwtService.extractUsername(token);
        // Assert
        assertEquals("User", username);
    }
    @Test
    void testIsTokenValid_ValidToken() {
        // Arrange
        String token = jwtService.generateToken(userDetails);
        // Act
        boolean isValid = jwtService.isTokenValid(token, userDetails);
        // Assert
        assertTrue(isValid);
    }
    @Test
    void testIsTokenValid_InvalidUsername() {
        // Arrange
        String token = jwtService.generateToken(userDetails);

        UserDetails differentUser = User.builder()
                .username("differentUser")
                .password("password")
                .authorities(Collections.emptyList())
                .build();
        // Act
        boolean isValid = jwtService.isTokenValid(token, differentUser);
        // Assert
        assertFalse(isValid);
    }
    @Test
    void testExtractExpiration() {
        // Arrange
        String token = jwtService.generateToken(userDetails);

        Date expiration = ReflectionTestUtils.invokeMethod(
                jwtService,
                "extractExpiration",
                token);
        // Assert
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }
    @Test
    void testIsTokenExpired_FreshToken() throws Exception {
        // Arrange
        String token = jwtService.generateToken(userDetails);

        // Act
        boolean isTokenExpired= ReflectionTestUtils.invokeMethod(
                jwtService,
                "isTokenExpired",
                token);
        // Assert
        assertFalse(isTokenExpired);
       /* Method isTokenExpiredMethod = JwtService.class
                .getDeclaredMethod("isTokenExpired", String.class);
        isTokenExpiredMethod.setAccessible(true);
        // Act
        boolean isExpired = (boolean) isTokenExpiredMethod.invoke(jwtService, token);
        // Assert
        assertFalse(isExpired);*/
    }
}
