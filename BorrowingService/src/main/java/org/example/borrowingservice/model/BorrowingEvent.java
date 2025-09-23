package org.example.borrowingservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowingEvent {
    private String eventType;
    private Long borrowingId;
    private Long userId;
    private String userEmail;
    private String userFirstName;
    private String userLastName;
    private Long bookId;
    private String bookTitle;
    private String bookIsbn;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String notes;
}
