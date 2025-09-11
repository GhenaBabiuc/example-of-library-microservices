package org.example.booksservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String biography;
    private String nationality;
    private Set<Long> bookIds;
}
