package com.example.UserManagement.controller;

import com.example.UserManagement.dto.thirdParty.ThirdPartyRequest;
import com.example.UserManagement.dto.thirdParty.ThirdPartyResponse;
import com.example.UserManagement.service.ThirdPartyApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/proxy")
public class ProxyController {

    @Autowired
    private ThirdPartyApiService thirdPartyApiService;

    @PostMapping("/forward")
    public ResponseEntity<ThirdPartyResponse> forwardRequest(@RequestBody ThirdPartyRequest request) {
        ThirdPartyResponse response = thirdPartyApiService.callThirdPartyApi(request);
        return ResponseEntity.ok(response);
    }
}