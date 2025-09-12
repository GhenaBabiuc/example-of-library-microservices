package org.example.booksservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.example.booksservice.model.dto.AuthorRecord;
import org.example.booksservice.model.dto.CreateAuthorRequest;
import org.example.booksservice.model.dto.UpdateAuthorRequest;
import org.example.booksservice.service.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/{id}")
    public ResponseEntity<AuthorRecord> getAuthorById(@PathVariable Long id) {
        AuthorRecord author = authorService.getAuthorById(id);

        return ResponseEntity.ok(author);
    }

    @GetMapping("/exist/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.existsById(id));
    }

    @GetMapping
    public ResponseEntity<Page<AuthorRecord>> getAllAuthors(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(50) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        Sort sort = Sort.by(sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<AuthorRecord> authors = authorService.getAllAuthors(pageable);

        return ResponseEntity.ok(authors);
    }

    @PostMapping
    public ResponseEntity<AuthorRecord> createAuthor(@Valid @RequestBody CreateAuthorRequest createAuthorRequest) {
        AuthorRecord createdAuthor = authorService.createAuthor(createAuthorRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdAuthor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorRecord> updateAuthor(@PathVariable Long id, @Valid @RequestBody UpdateAuthorRequest updateAuthorRequest) {
        AuthorRecord updatedAuthor = authorService.updateAuthor(id, updateAuthorRequest);

        return ResponseEntity.ok(updatedAuthor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);

        return ResponseEntity.noContent().build();
    }
}
