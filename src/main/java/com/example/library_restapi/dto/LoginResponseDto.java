package com.example.library_restapi.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Set;

@Data
@Builder
public class LoginResponseDto {

    private String token;
    private String username;
    private Set<String> roles;
}
