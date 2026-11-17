package com.example.tasks;
import jakarta.validation.constraints.NotBlank;

public record Task(
        long id,
        @NotBlank String title,
        boolean done
) {
}
