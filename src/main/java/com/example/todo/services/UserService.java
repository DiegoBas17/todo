package com.example.todo.services;

import com.example.todo.dto.UserDTO;
import com.example.todo.entities.User;
import com.example.todo.exceptions.MyEntityNotFoundException;
import com.example.todo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new MyEntityNotFoundException("Utente con questo username: " + username + " non trovato"));
    }

    public User findById(Long id_user) {
        return userRepository.findById(id_user).orElseThrow(() -> new MyEntityNotFoundException("Utente con questo id: " + id_user + " non trovato."));
    }

    public User saveUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.username());
        user.setPassword(passwordEncoder.encode(userDTO.password()));
        return userRepository.save(user);
    }
}
