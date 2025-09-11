package org.example.borrowingservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.borrowingservice.model.dto.BorrowingRecord;
import org.example.borrowingservice.model.dto.CreateBorrowingRequest;
import org.example.borrowingservice.model.dto.UpdateBorrowingRequest;
import org.example.borrowingservice.service.BorrowingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/borrowings")
@RequiredArgsConstructor
public class BorrowingController {

    private final BorrowingService borrowingService;

    @GetMapping("/{id}")
    public ResponseEntity<BorrowingRecord> getBorrowingById(@PathVariable Long id) {
        BorrowingRecord borrowing = borrowingService.getBorrowingById(id);
        return ResponseEntity.ok(borrowing);
    }

    @GetMapping
    public ResponseEntity<Page<BorrowingRecord>> getAllBorrowings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        Sort sort = Sort.by(sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<BorrowingRecord> borrowings = borrowingService.getAllBorrowings(pageable);

        return ResponseEntity.ok(borrowings);
    }

    @PostMapping
    public ResponseEntity<BorrowingRecord> createBorrowing(@Valid @RequestBody CreateBorrowingRequest createBorrowingRequest) {
        BorrowingRecord createdBorrowing = borrowingService.createBorrowing(createBorrowingRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdBorrowing);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BorrowingRecord> updateBorrowing(@PathVariable Long id, @Valid @RequestBody UpdateBorrowingRequest updateBorrowingRequest) {
        BorrowingRecord updatedBorrowing = borrowingService.updateBorrowing(id, updateBorrowingRequest);

        return ResponseEntity.ok(updatedBorrowing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBorrowing(@PathVariable Long id) {
        borrowingService.deleteBorrowing(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<BorrowingRecord>> getBorrowingsByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        Sort sort = Sort.by(sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<BorrowingRecord> borrowings = borrowingService.getBorrowingsByUserId(userId, pageable);

        return ResponseEntity.ok(borrowings);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<Page<BorrowingRecord>> getBorrowingsByBookId(
            @PathVariable Long bookId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        Sort sort = Sort.by(sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<BorrowingRecord> borrowings = borrowingService.getBorrowingsByBookId(bookId, pageable);

        return ResponseEntity.ok(borrowings);
    }

    @GetMapping("/overdue")
    public ResponseEntity<Page<BorrowingRecord>> getOverdueBorrowings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        Sort sort = Sort.by(sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<BorrowingRecord> borrowings = borrowingService.getOverdueBorrowings(pageable);

        return ResponseEntity.ok(borrowings);
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<BorrowingRecord> returnBook(@PathVariable Long id) {
        BorrowingRecord borrowing = borrowingService.returnBook(id);

        return ResponseEntity.ok(borrowing);
    }
}
