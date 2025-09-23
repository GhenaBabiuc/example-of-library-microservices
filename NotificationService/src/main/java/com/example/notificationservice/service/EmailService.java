package com.example.notificationservice.service;

import com.example.notificationservice.model.BorrowingEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${notification.email.from}")
    private String fromEmail;

    @Value("${notification.email.enabled:false}")
    private boolean emailEnabled;

    public void sendBorrowingNotification(BorrowingEvent event) {
        if (!emailEnabled) {
            log.info("Email notifications are disabled");
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(event.getUserEmail());
            message.setSubject(getSubjectByEventType(event.getEventType()));
            message.setText(getEmailBody(event));

            mailSender.send(message);
            log.info("Email sent successfully to {} for event: {}", event.getUserEmail(), event.getEventType());
        } catch (Exception e) {
            log.error("Failed to send email to {} for event: {}", event.getUserEmail(), event.getEventType(), e);
        }
    }

    private String getSubjectByEventType(String eventType) {
        return switch (eventType) {
            case "BOOK_BORROWED" -> "Book Borrowing Confirmation";
            case "BOOK_RETURNED" -> "Book Return Confirmation";
            case "BOOK_OVERDUE" -> "Overdue Book Reminder";
            default -> "Library Notification";
        };
    }

    private String getEmailBody(BorrowingEvent event) {
        StringBuilder body = new StringBuilder();
        body.append("Dear ").append(event.getUserFirstName())
                .append(" ").append(event.getUserLastName()).append(",\n\n");

        switch (event.getEventType()) {
            case "BOOK_BORROWED":
                body.append("You have successfully borrowed the book:\n")
                        .append("Title: ").append(event.getBookTitle()).append("\n")
                        .append("ISBN: ").append(event.getBookIsbn()).append("\n")
                        .append("Borrow Date: ").append(event.getBorrowDate()).append("\n")
                        .append("Due Date: ").append(event.getDueDate()).append("\n\n")
                        .append("Please return the book by the due date to avoid late fees.\n");
                break;
            case "BOOK_RETURNED":
                body.append("You have successfully returned the book:\n")
                        .append("Title: ").append(event.getBookTitle()).append("\n")
                        .append("ISBN: ").append(event.getBookIsbn()).append("\n")
                        .append("Return Date: ").append(event.getReturnDate()).append("\n\n")
                        .append("Thank you for using our library services!\n");
                break;
            case "BOOK_OVERDUE":
                body.append("The following book is overdue:\n")
                        .append("Title: ").append(event.getBookTitle()).append("\n")
                        .append("ISBN: ").append(event.getBookIsbn()).append("\n")
                        .append("Due Date: ").append(event.getDueDate()).append("\n\n")
                        .append("Please return the book as soon as possible to avoid additional fees.\n");
                break;
        }

        if (event.getNotes() != null && !event.getNotes().isEmpty()) {
            body.append("\nNotes: ").append(event.getNotes()).append("\n");
        }

        body.append("\nBest regards,\n")
                .append("Library Management System");

        return body.toString();
    }
}
