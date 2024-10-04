package com.pucpr.byteplace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pucpr.byteplace.dto.AuthenticationRequestDTO;
import com.pucpr.byteplace.dto.AuthenticationResponseDTO;
import com.pucpr.byteplace.model.User;
import com.pucpr.byteplace.services.AuthenticationService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService service;


    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDTO> register(@RequestBody User request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(@RequestBody AuthenticationRequestDTO request) {
        return ResponseEntity.ok(service.login(request));
    }
}