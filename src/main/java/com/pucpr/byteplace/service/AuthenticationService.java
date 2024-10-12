package com.pucpr.byteplace.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pucpr.byteplace.dto.AuthenticationRequestDTO;
import com.pucpr.byteplace.dto.AuthenticationResponseDTO;
import com.pucpr.byteplace.dto.UserUpdateRequestDTO;
import com.pucpr.byteplace.enums.AddressType;
import com.pucpr.byteplace.model.Address;
import com.pucpr.byteplace.model.User;
import com.pucpr.byteplace.repository.AddressRepository;
import com.pucpr.byteplace.repository.UserRepository;

import lombok.AllArgsConstructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private AddressRepository addressRepository;
    private JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponseDTO register(User request) {
        var user = new User();
        user.setName(request.getName());
        user.setUsername(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        
        if(request.getAddresses() != null){
            user.setAddresses(request.getAddresses());
            user.getAddresses().forEach(address -> address.setUser(user));
        }

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

        System.out.println("User "+ user.getEmail() +" logged in");
        System.out.println("Authorities:"+ user.getAuthorities());

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

    public Optional<User> getUserById(Long id) {
        
        return userRepository.findById(id);

    }

    public List<Address> getUserAddresses(Long userId, AddressType addressType) {
        
        if (addressType == null){
            
            return addressRepository.findByUserId(userId);
            
        } 

        return addressRepository.findByUserIdAndAddressType(userId, addressType);

    }

    public boolean isOwner(Long userId, String email) {
        
        Optional<User> user = userRepository.findById(userId);
        
        return user.isPresent() && user.get().getEmail().equals(email);

    }

    public void updateUser(UserUpdateRequestDTO updatedUser) {
        
        User user = userRepository.findById(updatedUser.getId()).get();
        user.setName(updatedUser.getName());

        if(updatedUser.getAddresses() != null){
    
            user.removeAllAddresses();
            updatedUser.getAddresses().forEach(address -> user.addAddress(address));
            
        }

        userRepository.save(user);

    }

    public void deleteUser(Long id) {
        
        userRepository.deleteById(id);

    }

    public void deleteAddress(Long userId, Long addressId) {

        addressRepository.deleteById(addressId);

    }

    public Address getAddress(Long addressId) {
        
        return addressRepository.findById(addressId).get();

    }

    public void updateAddress(Address newAddress) {
        
        addressRepository.save(newAddress);

    }

    public void addAddress(Long userId, Address newwAddress) {
       
        User user = userRepository.findById(userId).get();

        newwAddress.setUser(user);

        user.addAddress(newwAddress);

        userRepository.save(user);
        
    }

}
