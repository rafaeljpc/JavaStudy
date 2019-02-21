package io.github.rafaeljpc.server.service.service;

import io.github.rafaeljpc.server.service.entity.User;
import io.github.rafaeljpc.server.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service(value = "userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountStatusUserDetailsChecker checker;

    @Override
    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {
        Optional<User> user = null;

        if (input.contains("@")) {
            user = userRepository.findByEmail(input);
        }
        else {
            user = userRepository.findByUsername(input);
        }

        if (!user.isPresent())
            throw new BadCredentialsException("Bad credentials");

        checker.check(user.get());

        return user.get();
    }
}
