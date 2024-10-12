package com.pucpr.byteplace.controller;

import java.util.List;
import java.util.Optional;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pucpr.byteplace.dto.AuthenticationRequestDTO;
import com.pucpr.byteplace.dto.AuthenticationResponseDTO;
import com.pucpr.byteplace.dto.UserUpdateRequestDTO;
import com.pucpr.byteplace.enums.AddressType;
import com.pucpr.byteplace.model.Address;
import com.pucpr.byteplace.model.User;
import com.pucpr.byteplace.service.AuthenticationService;


@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService service) {
        this.authService = service;
    }


    /*
     * 
     * ENDPOINTS
     * 
     */

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDTO> register(@RequestBody User request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(@RequestBody AuthenticationRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("@authenticationService.isOwner(#id, #authentication.name)")
    public ResponseEntity<Optional<User>> getUserDetails(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(authService.getUserById(id));
    }

    @PutMapping("/user/{id}")
    @PreAuthorize("@authenticationService.isOwner(#id, #authentication.name)")
    public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequestDTO user,  Authentication authentication) {
        
        user.setId(id);

        authService.updateUser(user);

        return ResponseEntity.noContent().build();
        
    }

    @DeleteMapping("/user/{id}")
    @PreAuthorize("@authenticationService.isOwner(#id, #authentication.name)")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id, Authentication authentication) {
        
        authService.deleteUser(id);

        return ResponseEntity.noContent().build();

    }

    @GetMapping("/user/{id}/address")
    @PreAuthorize("@authenticationService.isOwner(#id, #authentication.name)")
    public ResponseEntity<List<Address>> getAddresses(@PathVariable Long id, @RequestParam(required = false) AddressType addressType, Authentication authentication) {
        return ResponseEntity.ok(authService.getUserAddresses(id, addressType));
    }

}