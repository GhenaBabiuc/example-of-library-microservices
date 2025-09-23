package org.example.borrowingservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.borrowingservice.kafka.BorrowingEventProducer;
import org.example.borrowingservice.mapper.BorrowingMapper;
import org.example.borrowingservice.model.BorrowStatus;
import org.example.borrowingservice.model.Borrowing;
import org.example.borrowingservice.model.BorrowingEvent;
import org.example.borrowingservice.model.dto.BorrowingRecord;
import org.example.borrowingservice.model.dto.CreateBorrowingRequest;
import org.example.borrowingservice.model.dto.UpdateBorrowingRequest;
import org.example.borrowingservice.repository.BorrowingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class BorrowingService {

    private final BorrowingRepository borrowingRepository;
    private final BorrowingMapper borrowingMapper;
    private final ExternalService externalService;
    private final BorrowingEventProducer eventProducer;

    public BorrowingRecord getBorrowingById(Long id) {
        log.debug("Fetching borrowing with id: {}", id);
        Borrowing borrowing = borrowingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrowing not found with id: " + id));

        return borrowingMapper.toRecord(borrowing);
    }

    public Page<BorrowingRecord> getAllBorrowings(Pageable pageable) {
        log.debug("Fetching all borrowings with pagination: {}", pageable);
        Page<Borrowing> borrowings = borrowingRepository.findAll(pageable);

        return borrowings.map(borrowingMapper::toRecord);
    }

    public Page<BorrowingRecord> getBorrowingsByUserId(Long userId, Pageable pageable) {
        log.debug("Fetching borrowings for user id: {} with pagination: {}", userId, pageable);
        Page<Borrowing> borrowings = borrowingRepository.findByUserId(userId, pageable);

        return borrowings.map(borrowingMapper::toRecord);
    }

    public Page<BorrowingRecord> getBorrowingsByBookId(Long bookId, Pageable pageable) {
        log.debug("Fetching borrowings for book id: {} with pagination: {}", bookId, pageable);
        Page<Borrowing> borrowings = borrowingRepository.findByBookId(bookId, pageable);

        return borrowings.map(borrowingMapper::toRecord);
    }

    public Page<BorrowingRecord> getOverdueBorrowings(Pageable pageable) {
        log.debug("Fetching overdue borrowings with pagination: {}", pageable);
        Page<Borrowing> borrowings = borrowingRepository.findOverdueBorrowings(LocalDate.now(), pageable);

        return borrowings.map(borrowingMapper::toRecord);
    }

    @Transactional
    public BorrowingRecord createBorrowing(CreateBorrowingRequest createBorrowingRequest) {
        log.debug("Creating borrowing for user: {} and book: {}",
                createBorrowingRequest.userId(), createBorrowingRequest.bookId());

        if (!externalService.userExists(createBorrowingRequest.userId())) {
            throw new RuntimeException("User not found with id: " + createBorrowingRequest.userId());
        }

        if (!externalService.bookExists(createBorrowingRequest.bookId())) {
            throw new RuntimeException("Book not found with id: " + createBorrowingRequest.bookId());
        }

        if (borrowingRepository.findActiveBorrowingByUserAndBook(
                createBorrowingRequest.userId(), createBorrowingRequest.bookId()).isPresent()) {
            throw new RuntimeException("User already has this book borrowed");
        }

        Borrowing borrowing = borrowingMapper.toEntity(createBorrowingRequest);
        borrowing.setBorrowDate(LocalDate.now());
        borrowing.setStatus(BorrowStatus.ACTIVE);

        if (borrowing.getDueDate() == null) {
            borrowing.setDueDate(LocalDate.now().plusDays(14));
        }

        Borrowing savedBorrowing = borrowingRepository.save(borrowing);
        log.info("Created borrowing with id: {}", savedBorrowing.getId());

        sendBorrowingEvent(savedBorrowing, "BOOK_BORROWED");

        return borrowingMapper.toRecord(savedBorrowing);
    }

    @Transactional
    public BorrowingRecord updateBorrowing(Long id, UpdateBorrowingRequest updateBorrowingRequest) {
        log.debug("Updating borrowing with id: {}", id);

        Borrowing existingBorrowing = borrowingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrowing not found with id: " + id));

        borrowingMapper.updateEntity(updateBorrowingRequest, existingBorrowing);

        Borrowing updatedBorrowing = borrowingRepository.save(existingBorrowing);
        log.info("Updated borrowing with id: {}", updatedBorrowing.getId());

        return borrowingMapper.toRecord(updatedBorrowing);
    }

    @Transactional
    public BorrowingRecord returnBook(Long id) {
        log.debug("Returning book for borrowing with id: {}", id);

        Borrowing borrowing = borrowingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrowing not found with id: " + id));

        if (borrowing.getStatus() != BorrowStatus.ACTIVE) {
            throw new RuntimeException("Book is not currently borrowed");
        }

        borrowing.setReturnDate(LocalDate.now());
        borrowing.setStatus(BorrowStatus.RETURNED);

        Borrowing updatedBorrowing = borrowingRepository.save(borrowing);
        log.info("Book returned for borrowing with id: {}", updatedBorrowing.getId());

        sendBorrowingEvent(updatedBorrowing, "BOOK_RETURNED");

        return borrowingMapper.toRecord(updatedBorrowing);
    }

    @Transactional
    public void deleteBorrowing(Long id) {
        log.debug("Deleting borrowing with id: {}", id);

        if (!borrowingRepository.existsById(id)) {
            throw new RuntimeException("Borrowing not found with id: " + id);
        }

        borrowingRepository.deleteById(id);
        log.info("Deleted borrowing with id: {}", id);
    }

    private void sendBorrowingEvent(Borrowing borrowing, String eventType) {
        try {
            var userInfo = externalService.getUserInfo(borrowing.getUserId());
            var bookInfo = externalService.getBookInfo(borrowing.getBookId());

            BorrowingEvent event = BorrowingEvent.builder()
                    .eventType(eventType)
                    .borrowingId(borrowing.getId())
                    .userId(borrowing.getUserId())
                    .userEmail(userInfo != null ? userInfo.email() : null)
                    .userFirstName(userInfo != null ? userInfo.firstName() : null)
                    .userLastName(userInfo != null ? userInfo.lastName() : null)
                    .bookId(borrowing.getBookId())
                    .bookTitle(bookInfo != null ? bookInfo.title() : null)
                    .bookIsbn(bookInfo != null ? bookInfo.isbn() : null)
                    .borrowDate(borrowing.getBorrowDate())
                    .dueDate(borrowing.getDueDate())
                    .returnDate(borrowing.getReturnDate())
                    .notes(borrowing.getNotes())
                    .build();

            eventProducer.sendBorrowingEvent(event);
        } catch (Exception e) {
            log.error("Failed to send borrowing event for borrowing id: {}", borrowing.getId(), e);
        }
    }
}
