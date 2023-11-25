package com.example.test_web_soscket.controller.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
public class User{
    @Id
    @GeneratedValue(generator = "user_gen",strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "user_seq",name = "user_gen",allocationSize = 1)
    private Long id;
    private String name;
    private String email;
    private String password;
    public User() {

    }
}
