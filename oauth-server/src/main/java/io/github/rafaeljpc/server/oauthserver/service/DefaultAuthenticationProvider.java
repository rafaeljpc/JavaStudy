package io.github.rafaeljpc.server.oauthserver.service;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class DefaultAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.getName() == null || authentication.getCredentials() == null) {
            return null;
        }

        final String userEmail = authentication.getName();
        final Object userPassword = authentication.getCredentials();

        if (userEmail == null || userPassword == null) {
            return null;
        }

        if (userEmail.isEmpty() || userPassword.toString().isEmpty()) {
            return null;
        }

        String validUserEmail = "test@test.com";
        String validUserPassword = "tester";

        if (!userEmail.equalsIgnoreCase(validUserEmail) || !userPassword.equals(validUserPassword)) {
            throw new UsernameNotFoundException("Invalid username or password");
        }

        return new UsernamePasswordAuthenticationToken(userEmail, userPassword, getAuthority());
    }

    private List<SimpleGrantedAuthority> getAuthority() {
        return Collections.emptyList();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
