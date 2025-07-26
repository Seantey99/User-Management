# User Management Service

This project provides a `UserService` class for managing user accounts in a Spring Boot application. It supports user registration, updating, deletion, and paginated retrieval, leveraging Spring Data JPA and best practices for transactional operations.

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Class Structure](#class-structure)
- [Endpoints & Methods](#endpoints--methods)
- [Error Handling](#error-handling)
- [Dependencies](#dependencies)
- [Postman Collection](#postman--collection)

---

## Overview

The `UserService` class encapsulates all business logic related to user management. It interacts with the `UserRepository` for database operations and provides clear, consistent responses using DTOs.

---

## Features

- **Register User**: Create new user accounts with validation for unique usernames.
- **Update User**: Modify user email and phone number.
- **Delete User**: Remove user accounts by username.
- **Paginated User Listing**: Retrieve users with pagination support.
- **Consistent Error Handling**: Uses HTTP status codes and descriptive messages.
- **Logging**: All operations are logged for traceability.

---

## Class Structure

- **UserService**: Main service class.
- **UserRepository**: JPA repository for `Users` entity.
- **Users**: Entity representing a user.
- **UserRequest**: DTO for incoming user data.
- **UserResponse**: DTO for outgoing user data.
- **PagingResult**: DTO for paginated results.
- **PaginationRequest**: DTO for pagination parameters.

---

## Endpoints & Methods

| Method         | Description                                   | Input DTO         | Output DTO        | HTTP Status Codes          |
|----------------|-----------------------------------------------|-------------------|-------------------|----------------------------|
| `registerUser` | Registers a new user                          | `UserRequest`     | `UserResponse`    | 200, 400, 500              |
| `updateUser`   | Updates email/phone for an existing user      | `UserRequest`     | `UserResponse`    | 200, 404, 500              |
| `deleteUser`   | Deletes a user by username                    | `UserRequest`     | `String` message  | 200, 404, 500              |
| `usersPaginated` | Returns paginated users (internal use)      | pageNo, pageSize  | `Page<Users>`     | 200                        |

---

## Error Handling
400 Bad Request: Duplicate username during registration.
404 Not Found: User not found for update or delete.
500 Internal Server Error: Unexpected exceptions during DB operations.
All errors are logged with descriptive messages.

---

## Dependencies
Spring Boot
Spring Data JPA
Lombok
SLF4J (Logging)

---

## Postman Collection
Postman Collection file located in src\main\resources\postman_collection

## Author
Tey Wei Sean
