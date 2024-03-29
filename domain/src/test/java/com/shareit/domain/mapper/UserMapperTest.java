package com.shareit.domain.mapper;

import com.shareit.domain.dto.Media;
import com.shareit.domain.entity.UserEntity;
import com.shareit.domain.dto.User;
import com.shareit.domain.entity.UserState;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    private final LocalDate birthDate = LocalDate.now();

    private final UserEntity userEntity = new UserEntity(1L,
            "any_email@mail.com",
            "any_password",
            "any_name",
            birthDate,
            UserState.CONFIRMED,
            null);

    private final User userModel = new User(1L,
            "any_name",
            birthDate,
            true,
            Media.builder().build());

    @Test
    public void testToModel() {
        User model = userMapper.toModel(userEntity, Media.builder().build());
        assertNotNull(model);
        assertEquals(userModel, model);
    }

}