package org.example.booksservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.booksservice.model.dto.BookDTO;
import org.example.booksservice.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        try {
            BookDTO book = bookService.getBookById(id);
            return ResponseEntity.ok(book);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDTO> getBookByIsbn(@PathVariable String isbn) {
        try {
            BookDTO book = bookService.getBookByIsbn(isbn);
            return ResponseEntity.ok(book);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) {
        try {
            BookDTO createdBook = bookService.createBook(bookDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO) {
        try {
            BookDTO updatedBook = bookService.updateBook(id, bookDTO);
            return ResponseEntity.ok(updatedBook);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookDTO>> searchBooks(@RequestParam String title) {
        List<BookDTO> books = bookService.searchBooks(title);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/by-language")
    public ResponseEntity<List<BookDTO>> getBooksByLanguage(@RequestParam String language) {
        List<BookDTO> books = bookService.getBooksByLanguage(language);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/by-publisher")
    public ResponseEntity<List<BookDTO>> getBooksByPublisher(@RequestParam String publisher) {
        List<BookDTO> books = bookService.getBooksByPublisher(publisher);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/by-author/{authorId}")
    public ResponseEntity<List<BookDTO>> getBooksByAuthor(@PathVariable Long authorId) {
        List<BookDTO> books = bookService.getBooksByAuthor(authorId);
        return ResponseEntity.ok(books);
    }
}