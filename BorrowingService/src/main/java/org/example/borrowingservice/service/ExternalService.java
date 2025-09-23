package org.example.borrowingservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.borrowingservice.client.BooksServiceClient;
import org.example.borrowingservice.client.UsersServiceClient;
import org.example.borrowingservice.model.dto.BookInfo;
import org.example.borrowingservice.model.dto.UserInfo;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalService {

    private final UsersServiceClient usersServiceClient;
    private final BooksServiceClient booksServiceClient;

    public boolean userExists(Long userId) {
        try {
            Boolean exists = usersServiceClient.existsById(userId);
            return exists != null && exists;
        } catch (Exception e) {
            log.error("Error checking if user exists with id: {}", userId, e);
            return false;
        }
    }

    public boolean bookExists(Long bookId) {
        try {
            Boolean exists = booksServiceClient.existsById(bookId);
            return exists != null && exists;
        } catch (Exception e) {
            log.error("Error checking if book exists with id: {}", bookId, e);
            return false;
        }
    }

    public UserInfo getUserInfo(Long userId) {
        try {
            return usersServiceClient.getUserById(userId);
        } catch (Exception e) {
            log.error("Error getting user info for id: {}", userId, e);
            return null;
        }
    }

    public BookInfo getBookInfo(Long bookId) {
        try {
            return booksServiceClient.getBookById(bookId);
        } catch (Exception e) {
            log.error("Error getting book info for id: {}", bookId, e);
            return null;
        }
    }
}
