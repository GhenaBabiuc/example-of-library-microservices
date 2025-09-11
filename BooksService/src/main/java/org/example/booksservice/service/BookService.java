package org.example.booksservice.service;

import lombok.RequiredArgsConstructor;
import org.example.booksservice.model.Author;
import org.example.booksservice.model.Book;
import org.example.booksservice.model.dto.BookDTO;
import org.example.booksservice.repository.AuthorRepository;
import org.example.booksservice.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        return convertToDTO(book);
    }

    public BookDTO getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new RuntimeException("Book not found with ISBN: " + isbn));
        return convertToDTO(book);
    }

    @Transactional
    public BookDTO createBook(BookDTO bookDTO) {
        Book book = convertToEntity(bookDTO);
        Book savedBook = bookRepository.save(book);
        return convertToDTO(savedBook);
    }

    @Transactional
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));

        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setIsbn(bookDTO.getIsbn());
        existingBook.setDescription(bookDTO.getDescription());
        existingBook.setPublicationDate(bookDTO.getPublicationDate());
        existingBook.setPages(bookDTO.getPages());
        existingBook.setLanguage(bookDTO.getLanguage());
        existingBook.setPublisher(bookDTO.getPublisher());

        if (bookDTO.getAuthorIds() != null && !bookDTO.getAuthorIds().isEmpty()) {
            Set<Author> authors = new HashSet<>(authorRepository.findAllById(bookDTO.getAuthorIds()));
            existingBook.setAuthors(authors);
        }

        Book updatedBook = bookRepository.save(existingBook);
        return convertToDTO(updatedBook);
    }

    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    public List<BookDTO> searchBooks(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookDTO> getBooksByLanguage(String language) {
        return bookRepository.findByLanguageIgnoreCase(language)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookDTO> getBooksByPublisher(String publisher) {
        return bookRepository.findByPublisherIgnoreCase(publisher)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookDTO> getBooksByAuthor(Long authorId) {
        return bookRepository.findBooksByAuthorId(authorId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private BookDTO convertToDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setIsbn(book.getIsbn());
        dto.setDescription(book.getDescription());
        dto.setPublicationDate(book.getPublicationDate());
        dto.setPages(book.getPages());
        dto.setLanguage(book.getLanguage());
        dto.setPublisher(book.getPublisher());
        dto.setAuthorIds(book.getAuthors().stream()
                .map(Author::getId)
                .collect(Collectors.toSet()));
        return dto;
    }

    private Book convertToEntity(BookDTO dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setDescription(dto.getDescription());
        book.setPublicationDate(dto.getPublicationDate());
        book.setPages(dto.getPages());
        book.setLanguage(dto.getLanguage());
        book.setPublisher(dto.getPublisher());

        if (dto.getAuthorIds() != null && !dto.getAuthorIds().isEmpty()) {
            Set<Author> authors = new HashSet<>(authorRepository.findAllById(dto.getAuthorIds()));
            book.setAuthors(authors);
        }

        return book;
    }
}