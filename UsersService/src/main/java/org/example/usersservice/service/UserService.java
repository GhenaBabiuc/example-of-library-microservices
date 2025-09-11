package org.example.usersservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.usersservice.mapper.UserMapper;
import org.example.usersservice.model.User;
import org.example.usersservice.model.UserStatus;
import org.example.usersservice.model.dto.CreateUserRequest;
import org.example.usersservice.model.dto.UpdateUserRequest;
import org.example.usersservice.model.dto.UserRecord;
import org.example.usersservice.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserRecord getUserById(Long id) {
        log.debug("Fetching user with id: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        return userMapper.toRecord(user);
    }

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    public Page<UserRecord> getAllUsers(Pageable pageable) {
        log.debug("Fetching all users with pagination: {}", pageable);
        Page<User> users = userRepository.findAll(pageable);

        return users.map(userMapper::toRecord);
    }

    @Transactional
    public UserRecord createUser(CreateUserRequest createUserRequest) {
        log.debug("Creating user with email: {}", createUserRequest.email());

        if (userRepository.existsByEmail(createUserRequest.email())) {
            throw new RuntimeException("User with email " + createUserRequest.email() + " already exists");
        }

        User user = userMapper.toEntity(createUserRequest);
        user.setMembershipDate(LocalDate.now());

        if (user.getStatus() == null) {
            user.setStatus(UserStatus.ACTIVE);
        }

        User savedUser = userRepository.save(user);
        log.info("Created user with id: {}", savedUser.getId());

        return userMapper.toRecord(savedUser);
    }

    @Transactional
    public UserRecord updateUser(Long id, UpdateUserRequest updateUserRequest) {
        log.debug("Updating user with id: {}", id);

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (!existingUser.getEmail().equals(updateUserRequest.email()) && userRepository.existsByEmail(updateUserRequest.email())) {
            throw new RuntimeException("User with email " + updateUserRequest.email() + " already exists");
        }

        userMapper.updateEntity(updateUserRequest, existingUser);
        User updatedUser = userRepository.save(existingUser);
        log.info("Updated user with id: {}", updatedUser.getId());

        return userMapper.toRecord(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        log.debug("Deleting user with id: {}", id);

        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
        log.info("Deleted user with id: {}", id);
    }
}
