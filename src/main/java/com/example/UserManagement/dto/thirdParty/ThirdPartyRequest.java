package com.example.UserManagement.dto.thirdParty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ThirdPartyRequest {
    private String username;
    private String password;
    private String email;
    private String phoneNumber;


    private Map<String, Object> extraFields;
}