package com.security.spring_jwt.controller;

import com.security.spring_jwt.dto.UserLogin;
import com.security.spring_jwt.dto.TokenResponse;
import com.security.spring_jwt.dto.UserDto;
import com.security.spring_jwt.entity.Role;
import com.security.spring_jwt.entity.User;
import com.security.spring_jwt.repository.UserRepository;
import com.security.spring_jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @PostMapping("/add")
    public String add(@RequestBody UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
        return "user created";
    }

    @PostMapping("/login")
    public TokenResponse authenticateservice(@RequestBody UserLogin request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = repository.findByEmail(request.getEmail()).get();
        var jwtToken = jwtService.generateToken(user);
        return TokenResponse.builder().token(jwtToken).build();
    }

}















     /*   @PostMapping("/register/admin")
    public ResponseEntity<TokenResponse> adminregister(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(service.adminregisterservice(userDto));
    }*/


 /*   @PostMapping("/login")
    public ResponseEntity<TokenResponse> loginrequest(@RequestBody UserLogin userLogin) {
        return ResponseEntity.ok(service.authenticateservice(userLogin));
    }
*/




/*
   @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(service.registerservice(userDto));
    }
*/



