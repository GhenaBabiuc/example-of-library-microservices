package org.example.borrowingservice.model.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateBorrowingRequest(
        @NotNull(message = "User ID is required")
        Long userId,

        @NotNull(message = "Book ID is required")
        Long bookId,

        @Future(message = "Due date must be in the future")
        LocalDate dueDate,

        @Size(max = 500, message = "Notes must not exceed 500 characters")
        String notes
) {
}
