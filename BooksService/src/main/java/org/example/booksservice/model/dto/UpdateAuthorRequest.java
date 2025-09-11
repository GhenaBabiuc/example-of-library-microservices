package org.example.booksservice.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateAuthorRequest(
        @NotBlank(message = "First name is required")
        @Size(max = 100, message = "First name must not exceed 100 characters")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(max = 100, message = "Last name must not exceed 100 characters")
        String lastName,

        LocalDate birthDate,

        @Size(max = 1000, message = "Biography must not exceed 1000 characters")
        String biography,

        @Size(max = 50, message = "Nationality must not exceed 50 characters")
        String nationality
) {
}
