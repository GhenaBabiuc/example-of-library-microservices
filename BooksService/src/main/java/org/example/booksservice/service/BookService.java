package org.example.booksservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.booksservice.mapper.BookMapper;
import org.example.booksservice.model.Author;
import org.example.booksservice.model.Book;
import org.example.booksservice.model.dto.BookRecord;
import org.example.booksservice.model.dto.CreateBookRequest;
import org.example.booksservice.model.dto.UpdateBookRequest;
import org.example.booksservice.repository.AuthorRepository;
import org.example.booksservice.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;

    public BookRecord getBookById(Long id) {
        log.debug("Fetching book with id: {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));

        return bookMapper.toRecord(book);
    }

    public boolean existsById(Long id) {
        return bookRepository.existsById(id);
    }

    public Page<BookRecord> getAllBooks(Pageable pageable) {
        log.debug("Fetching all books with pagination: {}", pageable);
        Page<Book> books = bookRepository.findAll(pageable);

        return books.map(bookMapper::toRecord);
    }

    @Transactional
    public BookRecord createBook(CreateBookRequest createBookRequest) {
        log.debug("Creating book with title: {}", createBookRequest.title());

        if (createBookRequest.isbn() != null && bookRepository.findByIsbn(createBookRequest.isbn()).isPresent()) {
            throw new RuntimeException("Book with ISBN " + createBookRequest.isbn() + " already exists");
        }

        Book book = bookMapper.toEntity(createBookRequest);

        if (createBookRequest.authorIds() != null && !createBookRequest.authorIds().isEmpty()) {
            Set<Author> authors = new HashSet<>(authorRepository.findAllById(createBookRequest.authorIds()));
            if (authors.size() != createBookRequest.authorIds().size()) {
                throw new RuntimeException("One or more authors not found");
            }
            book.setAuthors(authors);
        }

        Book savedBook = bookRepository.save(book);
        log.info("Created book with id: {}", savedBook.getId());

        return bookMapper.toRecord(savedBook);
    }

    @Transactional
    public BookRecord updateBook(Long id, UpdateBookRequest updateBookRequest) {
        log.debug("Updating book with id: {}", id);

        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));

        if (updateBookRequest.isbn() != null &&
                !updateBookRequest.isbn().equals(existingBook.getIsbn()) &&
                bookRepository.findByIsbn(updateBookRequest.isbn()).isPresent()) {
            throw new RuntimeException("Book with ISBN " + updateBookRequest.isbn() + " already exists");
        }

        bookMapper.updateEntity(updateBookRequest, existingBook);

        if (updateBookRequest.authorIds() != null) {
            if (updateBookRequest.authorIds().isEmpty()) {
                existingBook.getAuthors().clear();
            } else {
                Set<Author> authors = new HashSet<>(authorRepository.findAllById(updateBookRequest.authorIds()));
                if (authors.size() != updateBookRequest.authorIds().size()) {
                    throw new RuntimeException("One or more authors not found");
                }
                existingBook.setAuthors(authors);
            }
        }

        Book updatedBook = bookRepository.save(existingBook);
        log.info("Updated book with id: {}", updatedBook.getId());

        return bookMapper.toRecord(updatedBook);
    }

    @Transactional
    public void deleteBook(Long id) {
        log.debug("Deleting book with id: {}", id);

        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found with id: " + id);
        }

        bookRepository.deleteById(id);
        log.info("Deleted book with id: {}", id);
    }
}
