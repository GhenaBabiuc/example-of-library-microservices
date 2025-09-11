package org.example.borrowingservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalService {

    private final RestTemplate restTemplate;

    private static final String USERS_SERVICE_URL = "http://localhost:8083/users";
    private static final String BOOKS_SERVICE_URL = "http://localhost:8081/books";

    public boolean userExists(Long userId) {
        try {
            Boolean exists = restTemplate.getForObject(USERS_SERVICE_URL + "/exist/" + userId, Boolean.class);
            return exists != null && exists;
        } catch (Exception e) {
            log.error("Error checking if user exists with id: {}", userId, e);
            return false;
        }
    }

    public boolean bookExists(Long bookId) {
        try {
            Boolean exists = restTemplate.getForObject(BOOKS_SERVICE_URL + "/exist/" + bookId, Boolean.class);
            return exists != null && exists;
        } catch (Exception e) {
            log.error("Error checking if book exists with id: {}", bookId, e);
            return false;
        }
    }
}
