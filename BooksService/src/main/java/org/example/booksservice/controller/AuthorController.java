package org.example.booksservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.booksservice.model.dto.AuthorDTO;
import org.example.booksservice.service.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        List<AuthorDTO> authors = authorService.getAllAuthors();
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id) {
        try {
            AuthorDTO author = authorService.getAuthorById(id);
            return ResponseEntity.ok(author);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> createAuthor(@RequestBody AuthorDTO authorDTO) {
        try {
            AuthorDTO createdAuthor = authorService.createAuthor(authorDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAuthor);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable Long id, @RequestBody AuthorDTO authorDTO) {
        try {
            AuthorDTO updatedAuthor = authorService.updateAuthor(id, authorDTO);
            return ResponseEntity.ok(updatedAuthor);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        try {
            authorService.deleteAuthor(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<AuthorDTO>> searchAuthors(@RequestParam String name) {
        List<AuthorDTO> authors = authorService.searchAuthors(name);
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/by-nationality")
    public ResponseEntity<List<AuthorDTO>> getAuthorsByNationality(@RequestParam String nationality) {
        List<AuthorDTO> authors = authorService.getAuthorsByNationality(nationality);
        return ResponseEntity.ok(authors);
    }
}