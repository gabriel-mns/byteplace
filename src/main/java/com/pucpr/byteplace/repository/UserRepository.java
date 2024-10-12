package com.pucpr.byteplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pucpr.byteplace.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String username);

}
