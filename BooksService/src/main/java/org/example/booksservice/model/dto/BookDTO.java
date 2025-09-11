package org.example.booksservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Long id;
    private String title;
    private String isbn;
    private String description;
    private LocalDate publicationDate;
    private Integer pages;
    private String language;
    private String publisher;
    private Set<Long> authorIds;
}
