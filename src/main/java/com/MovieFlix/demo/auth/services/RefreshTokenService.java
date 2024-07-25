package com.MovieFlix.demo.auth.services;

import com.MovieFlix.demo.auth.entities.RefreshToken;
import com.MovieFlix.demo.auth.entities.User;
import com.MovieFlix.demo.auth.repositories.RefreshTokenRepository;
import com.MovieFlix.demo.auth.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

   private final UserRepository userRepository;
   private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(UserRepository userRepository , RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }


//    So in this function we check if refresh token of the user is present or not
    public RefreshToken createRefreshToken(String username){
   User user =  userRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email :" + username));
    RefreshToken refreshToken = user.getRefreshToken();

    //in  it is not present the we generate refresh token and save

    if(refreshToken == null){
//        long refreshTokenValidity = 5*60*60*1000;
        //for testinG
        long refreshTokenValidity = 30*1000;
        refreshToken = RefreshToken.builder().
                refreshToken(UUID.randomUUID().toString()).
                expirationTime(Instant.now().
                        plusMillis(refreshTokenValidity)).
                user(user)
                .build();

        refreshTokenRepository.save(refreshToken);

    }

//    if  it is  alreaddy present then we simply return the refresh token
    return refreshToken;

    }

    public RefreshToken verifyRefreshtoken(String refreshToken){
      RefreshToken refToken =   refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new RuntimeException("Refresh token not found"));

//      if refresh token expiore
        if(refToken.getExpirationTime().compareTo(Instant.now()) < 0){
            refreshTokenRepository.delete(refToken);
            throw  new RuntimeException("Refresh token expires");

        }
return refToken;
    }



}
