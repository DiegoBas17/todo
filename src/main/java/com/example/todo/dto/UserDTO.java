package com.example.todo.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserDTO(@NotEmpty(message = "L'email è obbligatoria")
                      String username,
                      @NotEmpty(message = "La password è obbligatoria!")
                      @Size(min = 3, max = 40)
                      String password) {
}
