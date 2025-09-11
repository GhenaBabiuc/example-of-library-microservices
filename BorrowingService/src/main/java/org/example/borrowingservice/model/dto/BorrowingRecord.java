package org.example.borrowingservice.model.dto;

import org.example.borrowingservice.model.BorrowStatus;

import java.time.LocalDate;

public record BorrowingRecord(
        Long id,
        Long userId,
        Long bookId,
        LocalDate borrowDate,
        LocalDate dueDate,
        LocalDate returnDate,
        BorrowStatus status,
        String notes
) {
}
