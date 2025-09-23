package org.example.borrowingservice.model.dto;

import java.time.LocalDate;
import java.util.Set;

public record BookInfo(
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
