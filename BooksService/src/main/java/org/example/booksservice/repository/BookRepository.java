package org.example.booksservice.repository;

import org.example.booksservice.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitleContainingIgnoreCase(String title);

    Optional<Book> findByIsbn(String isbn);

    List<Book> findByLanguageIgnoreCase(String language);

    List<Book> findByPublisherIgnoreCase(String publisher);

    @Query("SELECT DISTINCT b FROM Book b JOIN b.authors a WHERE a.id = :authorId")
    List<Book> findBooksByAuthorId(@Param("authorId") Long authorId);
}
