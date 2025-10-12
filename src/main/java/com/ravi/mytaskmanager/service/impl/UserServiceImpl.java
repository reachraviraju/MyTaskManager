package com.ravi.mytaskmanager.service.impl;

import com.ravi.mytaskmanager.dto.UserDTO;
import com.ravi.mytaskmanager.entity.Role;
import com.ravi.mytaskmanager.entity.User;
import com.ravi.mytaskmanager.exception.ResourceNotFoundException;
import com.ravi.mytaskmanager.exception.CustomAccessDeniedException;
import com.ravi.mytaskmanager.repository.UserRepository;
import com.ravi.mytaskmanager.service.AuthService;
import com.ravi.mytaskmanager.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthService authService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }

    @Override
    public UserDTO createUser(User user) {
        User currentUser = authService.getCurrentUser();
        if (currentUser.getRole() != Role.ADMIN) {
            throw new CustomAccessDeniedException("Only admin can create users.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return entityToDto(userRepository.save(user));
    }

    @Override
    public UserDTO registerUser(User user) {
        user.setRole(Role.USER); // Default role
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return entityToDto(userRepository.save(user));
    }

    @Override
    public List<UserDTO> getAllUsers() {
        User currentUser = authService.getCurrentUser();

        if (currentUser.getRole() == Role.ADMIN) {
            return userRepository.findAll().stream()
                    .map(this::entityToDto)
                    .collect(Collectors.toList());
        } else {
            return List.of(entityToDto(currentUser));
        }
    }

    @Override
    public UserDTO getUserById(Long id) {
        User currentUser = authService.getCurrentUser();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        if (currentUser.getRole() != Role.ADMIN && !currentUser.getId().equals(id)) {
            throw new CustomAccessDeniedException("You can only view your own profile.");
        }

        return entityToDto(user);
    }

    @Override
    public void deleteUser(Long id) {
        User currentUser = authService.getCurrentUser();
        if (currentUser.getRole() != Role.ADMIN) {
            throw new CustomAccessDeniedException("Only admin can delete users.");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        userRepository.delete(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    private UserDTO entityToDto(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }
}
