package org.example.borrowingservice.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.borrowingservice.model.BorrowStatus;

import java.time.LocalDate;

public record UpdateBorrowingRequest(
        LocalDate dueDate,

        LocalDate returnDate,

        @NotNull(message = "Status is required")
        BorrowStatus status,

        @Size(max = 500, message = "Notes must not exceed 500 characters")
        String notes
) {
}
