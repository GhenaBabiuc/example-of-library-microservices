package org.example.booksservice.service;

import lombok.RequiredArgsConstructor;
import org.example.booksservice.model.Author;
import org.example.booksservice.model.Book;
import org.example.booksservice.model.dto.AuthorDTO;
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
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AuthorDTO getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + id));
        return convertToDTO(author);
    }

    @Transactional
    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        Author author = convertToEntity(authorDTO);
        Author savedAuthor = authorRepository.save(author);
        return convertToDTO(savedAuthor);
    }

    @Transactional
    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) {
        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + id));

        existingAuthor.setFirstName(authorDTO.getFirstName());
        existingAuthor.setLastName(authorDTO.getLastName());
        existingAuthor.setBirthDate(authorDTO.getBirthDate());
        existingAuthor.setBiography(authorDTO.getBiography());
        existingAuthor.setNationality(authorDTO.getNationality());

        if (authorDTO.getBookIds() != null && !authorDTO.getBookIds().isEmpty()) {
            Set<Book> books = new HashSet<>(bookRepository.findAllById(authorDTO.getBookIds()));
            existingAuthor.setBooks(books);
        }

        Author updatedAuthor = authorRepository.save(existingAuthor);
        return convertToDTO(updatedAuthor);
    }

    @Transactional
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new RuntimeException("Author not found with id: " + id);
        }
        authorRepository.deleteById(id);
    }

    public List<AuthorDTO> searchAuthors(String name) {
        return authorRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AuthorDTO> getAuthorsByNationality(String nationality) {
        return authorRepository.findByNationalityIgnoreCase(nationality)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private AuthorDTO convertToDTO(Author author) {
        AuthorDTO dto = new AuthorDTO();
        dto.setId(author.getId());
        dto.setFirstName(author.getFirstName());
        dto.setLastName(author.getLastName());
        dto.setBirthDate(author.getBirthDate());
        dto.setBiography(author.getBiography());
        dto.setNationality(author.getNationality());
        dto.setBookIds(author.getBooks().stream()
                .map(Book::getId)
                .collect(Collectors.toSet()));
        return dto;
    }

    private Author convertToEntity(AuthorDTO dto) {
        Author author = new Author();
        author.setFirstName(dto.getFirstName());
        author.setLastName(dto.getLastName());
        author.setBirthDate(dto.getBirthDate());
        author.setBiography(dto.getBiography());
        author.setNationality(dto.getNationality());

        if (dto.getBookIds() != null && !dto.getBookIds().isEmpty()) {
            Set<Book> books = new HashSet<>(bookRepository.findAllById(dto.getBookIds()));
            author.setBooks(books);
        }

        return author;
    }
}