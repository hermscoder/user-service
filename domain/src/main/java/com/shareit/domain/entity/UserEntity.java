package com.shareit.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "s_user")
@Data
@AllArgsConstructor
public class UserEntity {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String email;
    private String password;
    private String name;
    private LocalDate birthDate;
    @Enumerated(EnumType.STRING)
    private UserState state;
    private Long profilePictureId;
//    private List<Interest> interests;

    public UserEntity() {
        this.state = UserState.CREATED;
    }

    public UserEntity(String email, String password, String name, LocalDate birthDate) {
        this(null,email, password, name, birthDate, UserState.CREATED, null);
    }

}
