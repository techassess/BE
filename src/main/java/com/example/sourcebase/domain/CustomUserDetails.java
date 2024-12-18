package com.example.sourcebase.domain;

import jakarta.transaction.Transactional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Transactional
public class CustomUserDetails extends User implements UserDetails {
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user, List<UserRole> roles) {
        this.setId(user.getId());
        this.setEmail(user.getEmail());
        this.setPhoneNumber(user.getPhoneNumber());
        this.setUsername(user.getUsername());
        this.setPassword(user.getPassword());
        this.setUserRoles(roles);

        List<GrantedAuthority> auths = new ArrayList<>();

        for(UserRole role : roles){
            auths.add(new SimpleGrantedAuthority(role.getRole().getName().toUpperCase()));
        }

        this.authorities = auths;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

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
}
