package io.github.rafaeljpc.server.oauthserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class DefaultUserDetailsService implements UserDetailsService {

    private static final String userName = "test@test.com";
    private static final String userPass = "tester";

    //@Autowired
    //private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return mockUser(username);
    }

    private UserDetails mockUser(String username) {
        if (!userName.equals(username)) {
            throw new UsernameNotFoundException("Invalid username or password");
        }

        UserDetails user = User.withDefaultPasswordEncoder()
                .username(username)
                .password(userPass)
                .authorities(getAuthority())
                .build();

        return user;
    }

    private List<SimpleGrantedAuthority> getAuthority() {
        return Collections.emptyList();
    }


}
