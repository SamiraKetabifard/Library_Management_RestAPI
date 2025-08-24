package com.example.library_restapi.service;
import com.example.library_restapi.dto.LoginRequestDto;
import com.example.library_restapi.dto.LoginResponseDto;
import com.example.library_restapi.dto.RegisterRequestDto;
import com.example.library_restapi.entity.User;
import com.example.library_restapi.jwt.JwtService;
import com.example.library_restapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;

    public User registerNormalUser(RegisterRequestDto registerRequestDto){
        if (userRepository.findByUsername(registerRequestDto.getUsername()).isPresent()){
            throw new RuntimeException("User Already registered");
        }
        Set<String> roles = new HashSet<String>();
        roles.add("ROLE_USER");

        User user = new User();
        user.setUsername(registerRequestDto.getUsername());
        user.setEmail(registerRequestDto.getEmail());
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));

        return userRepository.save(user);
    }

    public User registerAdminUser(RegisterRequestDto registerRequestDto){
        if (userRepository.findByUsername(registerRequestDto.getUsername()).isPresent()){
            throw new RuntimeException("User Already registered");
        }
        Set<String> roles = new HashSet<String>();
        //roles.add("ROLE_USER");
        roles.add("ROLE_ADMIN");

        User admin = new User();
        admin.setUsername(registerRequestDto.getUsername());
        admin.setEmail(registerRequestDto.getEmail());
        admin.setRoles(roles);
        admin.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));

        return userRepository.save(admin);
    }
    public LoginResponseDto login(LoginRequestDto loginRequestDto){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword())
        );

        User user = userRepository.findByUsername(loginRequestDto.getUsername())
                .orElseThrow(() ->new RuntimeException("user not found"));

        String token = jwtService.generateToken(user);

        return LoginResponseDto.builder()
                .token(token)
                .username(user.getUsername())
                .roles(user.getRoles())
                .build();
    }
}


