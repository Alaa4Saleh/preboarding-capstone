package com.example.tasks;

import jakarta.validation.constraints.NotBlank;

public record UpdateTaskRequest(
        @NotBlank String title,
        boolean done
) {
}
