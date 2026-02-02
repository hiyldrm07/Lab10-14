package com.sowa.halil57493.service;

import com.sowa.halil57493.controller.auth.RegisterRequest;
import com.sowa.halil57493.model.Role;
import com.sowa.halil57493.model.User;
import com.sowa.halil57493.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

        private final UserRepository repository;
        private final PasswordEncoder passwordEncoder;

        public User register(RegisterRequest request) {
                var user = new User();
                user.setUsername(request.getUsername());
                user.setEmail(request.getEmail());
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                user.setRole(Role.USER);

                log.info("User registered successfully: {}", request.getUsername());
                return repository.save(user);
        }
}
