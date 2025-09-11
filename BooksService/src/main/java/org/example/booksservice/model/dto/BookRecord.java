package org.example.booksservice.model.dto;

import java.time.LocalDate;
import java.util.Set;

public record BookRecord(
        Long id,
        String title,
        String isbn,
        String description,
        LocalDate publicationDate,
        Integer pages,
        String language,
        String publisher,
        Set<Long> authorIds
) {
}
