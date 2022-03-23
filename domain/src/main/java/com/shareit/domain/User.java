package com.shareit.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "s_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String email;
    private String password;
    private String name;
    private LocalDate birthDate;
//    private Media profilePicture;
//    private List<Interest> interests;


    public User(Long id, String email, String password, String name, LocalDate birthDate) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.birthDate = birthDate;
    }

    protected User() {

    }
}
