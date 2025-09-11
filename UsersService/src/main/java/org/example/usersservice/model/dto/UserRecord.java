package org.example.usersservice.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.usersservice.model.UserStatus;

import java.time.LocalDate;

public record UserRecord(
        Long id,

        @NotBlank(message = "First name is required")
        @Size(max = 100, message = "First name must not exceed 100 characters")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(max = 100, message = "Last name must not exceed 100 characters")
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        @Size(max = 150, message = "Email must not exceed 150 characters")
        String email,

        @Size(max = 20, message = "Phone must not exceed 20 characters")
        String phone,

        LocalDate birthDate,

        @Size(max = 500, message = "Address must not exceed 500 characters")
        String address,

        LocalDate membershipDate,

        UserStatus status
) {
}
