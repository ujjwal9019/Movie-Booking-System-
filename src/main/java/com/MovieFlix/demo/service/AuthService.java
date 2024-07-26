package com.MovieFlix.demo.service;

import com.MovieFlix.demo.auth.entities.User;
import com.MovieFlix.demo.auth.entities.UserRole;
import com.MovieFlix.demo.auth.repositories.UserRepository;
import com.MovieFlix.demo.auth.services.JwtService;
import com.MovieFlix.demo.auth.services.RefreshTokenService;
import com.MovieFlix.demo.auth.utils.AuthResponse;
import com.MovieFlix.demo.auth.utils.LoginRequest;
import com.MovieFlix.demo.auth.utils.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private  final PasswordEncoder passwordEncoder;

    private  final UserRepository userRepository;

    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;


    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest registerRequest){
        var user = User.builder().name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(UserRole.USER)
                .build();
        User savedUser = userRepository.save(user);
        var accessToken = jwtService.generateToken(savedUser);
        var refreshToken = refreshTokenService.createRefreshToken(savedUser.getEmail());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken()).build();

    }

    public AuthResponse login(LoginRequest loginRequest){
      authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                   loginRequest.getEmail(),
                   loginRequest.getPassword()
              )
      );
      var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not Found"));
       var accessToken = jwtService.generateToken(user);
       var refreshToken = refreshTokenService.createRefreshToken(loginRequest.getEmail());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken()).build();

    }
}
