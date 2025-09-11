package org.example.borrowingservice.repository;

import org.example.borrowingservice.model.Borrowing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing, Long>, JpaSpecificationExecutor<Borrowing> {

    Page<Borrowing> findByUserId(Long userId, Pageable pageable);

    Page<Borrowing> findByBookId(Long bookId, Pageable pageable);

    @Query("SELECT b FROM Borrowing b WHERE b.dueDate < :date AND b.status = 'ACTIVE'")
    Page<Borrowing> findOverdueBorrowings(@Param("date") LocalDate date, Pageable pageable);

    @Query("SELECT b FROM Borrowing b WHERE b.userId = :userId AND b.bookId = :bookId AND b.status = 'ACTIVE'")
    Optional<Borrowing> findActiveBorrowingByUserAndBook(@Param("userId") Long userId, @Param("bookId") Long bookId);

    @Query("SELECT COUNT(b) FROM Borrowing b WHERE b.bookId = :bookId AND b.status = 'ACTIVE'")
    Long countActiveBorrowingsByBookId(@Param("bookId") Long bookId);
}
