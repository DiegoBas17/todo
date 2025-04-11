package com.example.todo.controllers;

import com.example.todo.dto.NewEntityRespDTO;
import com.example.todo.dto.UserDTO;
import com.example.todo.dto.UserLoginRespDTO;
import com.example.todo.entities.User;
import com.example.todo.exceptions.MyEntityNotFoundException;
import com.example.todo.services.AuthService;
import com.example.todo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public UserLoginRespDTO login(@RequestBody @Validated UserDTO payload) {
        User user = userService.findByUsername(payload.username());
        return new UserLoginRespDTO(this.authService.checkCredentialsAndGenerateToken(payload), payload.username());
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public NewEntityRespDTO save(@RequestBody @Validated UserDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new MyEntityNotFoundException("Ci sono stati errori nel payload. " + messages);
        } else {
            return new NewEntityRespDTO(this.userService.saveUser(body).getId());
        }
    }

    @GetMapping("/me")
    public User getCurrentUser(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new MyEntityNotFoundException("Token mancante o non valido");
        }
        String token = authorizationHeader.substring(7);
        User currentUser = authService.getUserFromToken(token);
        return currentUser;
    }
}