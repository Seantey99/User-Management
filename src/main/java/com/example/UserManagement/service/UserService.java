package com.example.UserManagement.service;

import com.example.UserManagement.dto.user.UserRequest;
import com.example.UserManagement.dto.user.UserResponse;
import com.example.UserManagement.entity.Users;
import com.example.UserManagement.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
public class UserService {
    String message;

    @Autowired private UserRepository userRepository;

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    @Transactional
    public ResponseEntity<UserResponse> registerUser(UserRequest userRequest) {
        String username = userRequest.getUsername();
        String email = userRequest.getEmail();
        String phoneNumber = userRequest.getPhoneNumber();

        // Check if username already exists
        if (userRepository.findByName(username) != null) {
            message = "Username already exists";
            log.error(message);
            UserResponse response = new UserResponse();
            response.setMessage(message);
            return ResponseEntity.badRequest().body(response);
        }

        try {
            Users users = new Users();
            users.setId(UUID.randomUUID());
            users.setName(username);
            users.setPhone(phoneNumber);
            users.setEmail(email);

            userRepository.save(users);

            message = "Users registered successfully";
            UserResponse response = new UserResponse(
                    users.getName(),
                    users.getEmail(),
                    users.getPhone()
            );
            response.setMessage(message);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            message = "Failed to create user: " + e.getMessage();
            log.error(message);
            UserResponse response = new UserResponse();
            response.setMessage(message);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Transactional
    public ResponseEntity<String> deleteUser(UserRequest userRequest) {
        String username = userRequest.getUsername();

        try {
            if (userRepository.findByName(username) != null) {
                UUID id = userRepository.findByName(username).getId();
                userRepository.deleteById(id);
                String message = "Users Account Deleted Successfully.";
                log.info("User '{}' deleted successfully.", username);
                return ResponseEntity.ok(message);
            } else {
                String message = "User not found: " + username;
                log.warn(message);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
            }
        } catch (Exception e) {
            String message = "Failed to delete user: " + e.getMessage();
            log.error("Failed to delete user '{}': {}", username, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }

    @Transactional
    public ResponseEntity<UserResponse> updateUser(UserRequest userRequest) {
        String username = userRequest.getUsername();
        try {
            Users users = userRepository.findByName(username);
            if (users == null) {
                message = "User not found: " + username;
                log.warn(message);
                UserResponse response = new UserResponse();
                response.setMessage(message);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            if (!isNullOrEmpty(userRequest.getEmail())) {
                users.setEmail(userRequest.getEmail());
            }
            if (!isNullOrEmpty(userRequest.getPhoneNumber())) {
                users.setPhone(userRequest.getPhoneNumber());
            }

            userRepository.save(users);

            message = "User updated successfully";
            UserResponse response = new UserResponse(
                    users.getName(),
                    users.getEmail(),
                    users.getPhone()
            );
            response.setMessage(message);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            message = "Failed to update user: " + e.getMessage();
            log.error(message);
            UserResponse response = new UserResponse();
            response.setMessage(message);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Transactional(readOnly = true)
    public Page<Users> usersPaginated(int pageNo) {
        final int PAGE_SIZE = 2;
        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE);
        return userRepository.findAll(pageable);
    }
}
