package com.pucpr.byteplace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pucpr.byteplace.enums.AddressType;
import com.pucpr.byteplace.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUserId(Long userId);

    List<Address> findByUserIdAndAddressType(Long userId, AddressType addressType);
    
}
