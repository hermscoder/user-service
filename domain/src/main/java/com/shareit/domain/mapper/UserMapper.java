package com.shareit.domain.mapper;

import com.shareit.domain.dto.Media;
import com.shareit.domain.dto.registration.UserRegistration;
import com.shareit.domain.entity.UserEntity;
import com.shareit.domain.dto.User;
import com.shareit.domain.entity.UserState;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "name", source = "entity.name")
    @Mapping(target = "birthDate", source = "entity.birthDate")
    @Mapping(target = "confirmed", source = "entity.state", qualifiedByName = "isUserConfirmed")
    @Mapping(target = "profilePicture", source = "profilePicture")
    User toModel(UserEntity entity, Media profilePicture);

    UserEntity toEntity(UserRegistration userRegistration);

    @Named("isUserConfirmed")
    default Boolean isUserConfirmed(UserState userState) {
        return UserState.CONFIRMED.equals(userState);
    }
}
