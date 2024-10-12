package com.pucpr.byteplace.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pucpr.byteplace.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "USUARIO")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    @Setter
    private String password;
    @OneToMany(mappedBy      = "user",
               cascade       = CascadeType.ALL,
               fetch         = FetchType.EAGER,
               orphanRemoval = true)
    @JsonManagedReference
    private List<Address> addresses;
    @Enumerated(EnumType.ORDINAL)
    private Role role;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = role.getPermissionsList().stream().map(
                permissionEnum -> new SimpleGrantedAuthority(permissionEnum.name())
        ).collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));


        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public void setUsername(String username) {
        this.email = username;
    }

    public void addAddress(Address address) {

        address.setUser(this);

        addresses.add(address);

    }

    public void removeAddress(Address address) {

        addresses.remove(address);

    }

    public void removeAllAddresses() {

        addresses.clear();

    }

}
