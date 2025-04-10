package com.example.todo.security;

import com.example.todo.entities.User;
import com.example.todo.exceptions.MyEntityNotFoundException;
import com.example.todo.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTCheckFilter extends OncePerRequestFilter {
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new MyEntityNotFoundException("Per favore inserisci correttamente il token nell'Authorization Header");

        String accessToken = authHeader.substring(7);
        jwtTools.verifyToken(accessToken); // Verifica il token

        String id = jwtTools.extractIdFromToken(accessToken);
        User currentUser = userService.findById(Long.valueOf(id));

        Authentication authentication = new UsernamePasswordAuthenticationToken(currentUser, null, currentUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/api/auth/**", request.getServletPath()); // Non applicare il filtro sulle rotte /api/auth/**
    }
}
