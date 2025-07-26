package com.example.UserManagement.controller;

import com.example.UserManagement.dto.user.UserRequest;
import com.example.UserManagement.dto.user.UserResponse;
import com.example.UserManagement.entity.Users;
import com.example.UserManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static io.netty.util.internal.StringUtil.isNullOrEmpty;

@RestController
@RequestMapping("/v1/api/users")
public class UserController {
    String RESPONSE_MESSAGE = "All fields cannot be null or empty";
    String USERNAME_MESSAGE = "Username must not be null or empty.";
    // This class will handle user-related operations such as registration, login, and profile management.
    // It will interact with the UserService to perform these operations.
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserRequest userRequest) {
        String username = userRequest.getUsername();
        String password = userRequest.getPassword();
        String email = userRequest.getEmail();
        String phoneNumber = userRequest.getPhoneNumber();

        if (isNullOrEmpty(username) || isNullOrEmpty(password) || isNullOrEmpty(email) || isNullOrEmpty(phoneNumber)) {
            UserResponse response = new UserResponse();
            response.setMessage(RESPONSE_MESSAGE);
            return ResponseEntity.badRequest().body(response);
        }

        return userService.registerUser(userRequest);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestBody UserRequest userRequest) {
        String username = userRequest.getUsername();

        if (isNullOrEmpty(username)) {
            return ResponseEntity.badRequest().body(USERNAME_MESSAGE);
        }
        return userService.deleteUser(userRequest);
    }

    @PutMapping("/update")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest userRequest) {
        String username = userRequest.getUsername();
        String password = userRequest.getPassword();
        String email = userRequest.getEmail();
        String phoneNumber = userRequest.getPhoneNumber();

        if (isNullOrEmpty(username) || isNullOrEmpty(password) || isNullOrEmpty(email) || isNullOrEmpty(phoneNumber)) {
            UserResponse response = new UserResponse();
            response.setMessage(RESPONSE_MESSAGE);
            return ResponseEntity.badRequest().body(response);
        }

        return userService.updateUser(userRequest);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<Users>> getPaginatedProducts(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {

        Page<Users> productsPage = userService.usersPaginated(pageNo);
        return new ResponseEntity<>(productsPage, HttpStatus.OK);
    }
}
