package com.solbeg.nmailservice.security;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.solbeg.nmailservice.security.deserializer.UserDetailsImplDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonDeserialize(using = UserDetailsImplDeserializer.class)
public class UserDetailsImpl implements UserDetails {
    private String username;
    private String password;
    private Set<SimpleGrantedAuthority> authorities;
    private boolean isActive;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
