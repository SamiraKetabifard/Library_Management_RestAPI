package com.example.library_restapi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequestDto {

    private String username;
    private String email;
    private String password;
}
