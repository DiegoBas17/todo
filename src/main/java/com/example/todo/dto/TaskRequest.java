package com.example.todo.dto;

public record TaskRequest(String title,
                          String description,
                          String completed) {
}
