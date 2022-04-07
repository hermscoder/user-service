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

    public User(String email, String password, String name, LocalDate birthDate) {
        this(null,email, password, name, birthDate);
    }

    public User(Long id, String email, String password, String name, LocalDate birthDate) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.birthDate = birthDate;
    }

    protected User() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
