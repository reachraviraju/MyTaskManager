package com.ravi.mytaskmanager.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, nullable=false)
    private String username;

    @Column(nullable=true)
    private String email;

    // stored as BCrypt hash in production 
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}
