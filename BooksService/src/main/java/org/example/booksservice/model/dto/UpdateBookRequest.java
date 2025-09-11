package org.example.booksservice.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;

public record UpdateBookRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must not exceed 255 characters")
        String title,

        @Size(max = 17, message = "ISBN must not exceed 17 characters")
        String isbn,

        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        String description,

        LocalDate publicationDate,

        @Min(value = 1, message = "Pages must be at least 1")
        Integer pages,

        @Size(max = 10, message = "Language must not exceed 10 characters")
        String language,

        @Size(max = 150, message = "Publisher must not exceed 150 characters")
        String publisher,

        Set<Long> authorIds
) {
}
