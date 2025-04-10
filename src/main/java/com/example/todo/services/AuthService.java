package com.example.todo.services;

import com.example.todo.dto.UserDTO;
import com.example.todo.entities.User;
import com.example.todo.exceptions.MyIllegalException;
import com.example.todo.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserService userService;
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private PasswordEncoder bcrypt;

    public String checkCredentialsAndGenerateToken(UserDTO body) {
        User found = this.userService.findByUsername(body.username());
        if (bcrypt.matches(body.password(), found.getPassword())) {
            return jwtTools.createToken(found);
        } else {
            throw new MyIllegalException("Credenziali errate!");
        }
    }

    public User getUserFromToken(String token) {
        jwtTools.verifyToken(token);
        String userId = jwtTools.extractIdFromToken(token);
        return userService.findById(Long.valueOf(userId));
    }
}
