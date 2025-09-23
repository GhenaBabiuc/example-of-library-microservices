package org.example.borrowingservice.model.dto;

import org.example.borrowingservice.model.UserStatus;

import java.time.LocalDate;

public record UserInfo(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        LocalDate birthDate,
        String address,
        LocalDate membershipDate,
        UserStatus status
) {
}
