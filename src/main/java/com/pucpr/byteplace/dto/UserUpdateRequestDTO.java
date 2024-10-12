package com.pucpr.byteplace.dto;

import java.util.List;

import com.pucpr.byteplace.model.Address;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserUpdateRequestDTO {

    private Long id;
    private String name;
    private List<Address> addresses;

}
