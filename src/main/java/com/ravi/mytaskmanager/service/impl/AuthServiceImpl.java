package com.ravi.mytaskmanager.service.impl;

import com.ravi.mytaskmanager.entity.User;
import com.ravi.mytaskmanager.exception.ResourceNotFoundException;
import com.ravi.mytaskmanager.repository.UserRepository;
import com.ravi.mytaskmanager.service.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }
}
