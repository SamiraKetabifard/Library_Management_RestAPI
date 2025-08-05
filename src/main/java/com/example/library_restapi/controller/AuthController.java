package com.example.library_restapi.controller;

import com.example.library_restapi.dto.LoginRequestDto;
import com.example.library_restapi.dto.LoginResponseDto;
import com.example.library_restapi.dto.RegisterRequestDto;
import com.example.library_restapi.entity.User;
import com.example.library_restapi.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/registeruser")
    public ResponseEntity<User> registerNormalUser(@RequestBody RegisterRequestDto registerRequestDto){
        return ResponseEntity.ok(authenticationService.registerNormalUser(registerRequestDto));
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto){
        return ResponseEntity.ok(authenticationService.login(loginRequestDto));
    }
}
