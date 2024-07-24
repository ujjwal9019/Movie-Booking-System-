package com.MovieFlix.demo.auth.services;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthFilterService extends OncePerRequestFilter {


    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    public AuthFilterService(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

//        So we extract acess token for authHeader
    final String authHeader = request.getHeader("Authorization");

    String jwt;
    String username;


    //agar access token hai hi nhi header me to just filter it ahead and return from there
    if(authHeader == null || !authHeader.startsWith("Bearer")){
        filterChain.doFilter(request,response);
        return;
    }
    //incase if it present then we extract it

        //etract jwt
        jwt = authHeader.substring(7);

    //extract username from jwt

        username = jwtService.extractUsername(jwt);


        //user is not authenticated yet and username is not null

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if(jwtService.isTokenValid(jwt , userDetails)){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails ,
                        null,
                        userDetails.getAuthorities()
                );
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        }

        filterChain.doFilter(request,response);


    }
}
