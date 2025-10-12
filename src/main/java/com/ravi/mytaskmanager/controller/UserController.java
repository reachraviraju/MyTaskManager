package com.ravi.mytaskmanager.controller;

import com.ravi.mytaskmanager.dto.UserDTO;
import com.ravi.mytaskmanager.entity.User;
import com.ravi.mytaskmanager.service.AuthService;
import com.ravi.mytaskmanager.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping
    public UserDTO createUser(@RequestBody User user) {
    	
        User currentUser = authService.getCurrentUser();
        
        if (currentUser.getRole() != com.ravi.mytaskmanager.entity.Role.ADMIN) {
            throw new RuntimeException("Only admin can create users.");
        }
        return userService.createUser(user);
    }

    @PostMapping("/register")
    public UserDTO registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
    	
        User currentUser = authService.getCurrentUser();
        
        if (currentUser.getRole() != com.ravi.mytaskmanager.entity.Role.ADMIN) {
            throw new RuntimeException("Only admin can view all users.");
        }
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
    	
        User currentUser = authService.getCurrentUser();
        
        if (currentUser.getRole() != com.ravi.mytaskmanager.entity.Role.ADMIN && !currentUser.getId().equals(id)) {
            throw new RuntimeException("You can only view your own profile.");
        }
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
    	
        User currentUser = authService.getCurrentUser();
        
        if (currentUser.getRole() != com.ravi.mytaskmanager.entity.Role.ADMIN) {
            throw new RuntimeException("Only admin can delete users.");
        }
        userService.deleteUser(id);
    }
}
