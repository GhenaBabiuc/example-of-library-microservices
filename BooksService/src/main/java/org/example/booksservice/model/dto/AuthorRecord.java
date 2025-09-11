package org.example.booksservice.model.dto;

import java.time.LocalDate;

public record AuthorRecord(
        Long id,
        String firstName,
        String lastName,
        LocalDate birthDate,
        String biography,
        String nationality
) {
}
