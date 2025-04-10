package com.example.todo.services;

import com.example.todo.entities.User;
import com.example.todo.exceptions.NotFoundException;
import com.example.todo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Utente con questo username: " + username + " non trovato"));
    }

    /*public User saveUser(UserRequest user) {

    }*/
}
