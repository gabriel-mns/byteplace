package com.pucpr.byteplace.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pucpr.byteplace.dto.AuthenticationRequestDTO;
import com.pucpr.byteplace.dto.AuthenticationResponseDTO;
import com.pucpr.byteplace.model.User;
import com.pucpr.byteplace.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;


    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public AuthenticationResponseDTO register(User request) {
        var user = new User();
        user.setName(request.getName());
        user.setUsername(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setAddresses(request.getAddresses());
        user.getAddresses().forEach(address -> address.setUser(user));

        userRepository.save(user);

        String token = jwtService.generateToken(user, generateExtraClaims(user));
        
        return new AuthenticationResponseDTO(token);
    }

    public AuthenticationResponseDTO login(AuthenticationRequestDTO authenticationRequestDTO) {

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                authenticationRequestDTO.getEmail(), authenticationRequestDTO.getPassword()
        );

        authenticationManager.authenticate(authToken);

        User user = userRepository.findByEmail(authenticationRequestDTO.getEmail()).get();

        String token = jwtService.generateToken(user, generateExtraClaims(user));

        return new AuthenticationResponseDTO(token);

    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    private Map<String, Object> generateExtraClaims(User user) {

        Map<String, Object> extraClaims = new HashMap<>();
        
        extraClaims.put("email", user.getEmail());
        extraClaims.put("name", user.getName());
        extraClaims.put("role", user.getRole().name());
        
        return extraClaims;
        
    }
}
