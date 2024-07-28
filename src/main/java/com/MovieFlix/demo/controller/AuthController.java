package com.MovieFlix.demo.controller;

import com.MovieFlix.demo.auth.entities.RefreshToken;
import com.MovieFlix.demo.auth.entities.User;
import com.MovieFlix.demo.auth.services.JwtService;
import com.MovieFlix.demo.auth.services.RefreshTokenService;
import com.MovieFlix.demo.auth.utils.AuthResponse;
import com.MovieFlix.demo.auth.utils.LoginRequest;
import com.MovieFlix.demo.auth.utils.RefreshTokenRequest;
import com.MovieFlix.demo.auth.utils.RegisterRequest;
import com.MovieFlix.demo.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

private final AuthService authService;
private  final RefreshTokenService refreshTokenService;

private final JwtService jwtService;

    public AuthController(AuthService authService, RefreshTokenService refreshTokenService, JwtService jwtService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login (@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/refresh")
public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
    RefreshToken refreshToken =  refreshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());

    User user =  refreshToken.getUser();

    String accessToken = jwtService.generateToken(user);

    return  ResponseEntity.ok(AuthResponse.builder().
            accessToken(accessToken).
            refreshToken(refreshToken.getRefreshToken()).build());
    }

}
