package com.example.UserManagement.dto;

import com.example.UserManagement.dto.user.UserResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Data
public class BaseResponse<T> implements Serializable {
    private static final String SUCCESS_MESSAGE = "Success";
    private static final String FAILURE_MESSAGE = "FAILED";

    private String status;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String message;
    private T data;

    public BaseResponse() {
        // Default constructor
    }

    public BaseResponse(UserResponse user) {
    }

    public BaseResponse(String message, String status) {
        this.message = message;
        this.status = status;
    }

    public BaseResponse(String status, String message, T data) {
        this.data = data;
        this.message = message;
        this.status = status;
    }

    public static <T> BaseResponse<T> successResponse(T data) {
        return new BaseResponse<>(SUCCESS_MESSAGE, "", data);
    }

    public static <T> BaseResponse<T> failureResponse(String message, T data) {
        return new BaseResponse<>(FAILURE_MESSAGE, "", data);
    }
}
