package org.example.booksservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.booksservice.mapper.AuthorMapper;
import org.example.booksservice.model.Author;
import org.example.booksservice.model.dto.AuthorRecord;
import org.example.booksservice.model.dto.CreateAuthorRequest;
import org.example.booksservice.model.dto.UpdateAuthorRequest;
import org.example.booksservice.repository.AuthorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorRecord getAuthorById(Long id) {
        log.debug("Fetching author with id: {}", id);
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + id));

        return authorMapper.toRecord(author);
    }

    public boolean existsById(Long id) {
        return authorRepository.existsById(id);
    }

    public Page<AuthorRecord> getAllAuthors(Pageable pageable) {
        log.debug("Fetching all authors with pagination: {}", pageable);
        Page<Author> authors = authorRepository.findAll(pageable);

        return authors.map(authorMapper::toRecord);
    }

    @Transactional
    public AuthorRecord createAuthor(CreateAuthorRequest createAuthorRequest) {
        log.debug("Creating author with name: {} {}", createAuthorRequest.firstName(), createAuthorRequest.lastName());

        Author author = authorMapper.toEntity(createAuthorRequest);
        Author savedAuthor = authorRepository.save(author);
        log.info("Created author with id: {}", savedAuthor.getId());

        return authorMapper.toRecord(savedAuthor);
    }

    @Transactional
    public AuthorRecord updateAuthor(Long id, UpdateAuthorRequest updateAuthorRequest) {
        log.debug("Updating author with id: {}", id);

        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + id));

        authorMapper.updateEntity(updateAuthorRequest, existingAuthor);
        Author updatedAuthor = authorRepository.save(existingAuthor);
        log.info("Updated author with id: {}", updatedAuthor.getId());

        return authorMapper.toRecord(updatedAuthor);
    }

    @Transactional
    public void deleteAuthor(Long id) {
        log.debug("Deleting author with id: {}", id);

        if (!authorRepository.existsById(id)) {
            throw new RuntimeException("Author not found with id: " + id);
        }

        authorRepository.deleteById(id);
        log.info("Deleted author with id: {}", id);
    }
}
