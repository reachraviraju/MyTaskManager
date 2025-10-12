package com.ravi.mytaskmanager.service;

import com.ravi.mytaskmanager.dto.UserDTO;
import com.ravi.mytaskmanager.entity.User;
import java.util.List;

public interface UserService {
    
   
	
    
    UserDTO createUser(User user);         // Admin creates user
    
    UserDTO registerUser(User user);       // Public registration, default role USER
    
    List<UserDTO> getAllUsers();           // Admin sees all, user sees only themselves
    
    UserDTO getUserById(Long id);          // Admin sees any, user sees only themselves
    
    void deleteUser(Long id);              // Admin deletes user
    
    User findByUsername(String username);  // Internal use
    
}
