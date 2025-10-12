package com.ravi.mytaskmanager.dto;

import com.ravi.mytaskmanager.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data 
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	
    
	private Long id;
    private String username;
    private String email;
    private Role role;
    
    
    
}

