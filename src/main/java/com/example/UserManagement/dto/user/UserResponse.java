package com.example.UserManagement.dto.user;

import com.example.UserManagement.dto.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse extends BaseResponse {
    private String username;
    private String email;
    private String phoneNumber;

    public UserResponse(String message) {
    }
}
