package org.example.booksservice.repository;

import org.example.booksservice.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);

    List<Author> findByNationalityIgnoreCase(String nationality);

    @Query("SELECT DISTINCT a FROM Author a JOIN a.books b WHERE b.id = :bookId")
    List<Author> findAuthorsByBookId(@Param("bookId") Long bookId);
}
