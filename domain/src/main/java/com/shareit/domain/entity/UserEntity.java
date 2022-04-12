package com.shareit.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "s_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String email;
    private String password;
    private String name;
    private LocalDate birthDate;
    @Enumerated(EnumType.STRING)
    private UserState state;
//    private Media profilePicture;
//    private List<Interest> interests;

    public UserEntity(String email, String password, String name, LocalDate birthDate) {
        this(null,email, password, name, birthDate, UserState.CREATED);
    }

}
