package com.shareit.domain.mapper;

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

    @Mapping(target = "confirmed", source = "state", qualifiedByName = "isUserConfirmed")
    User toModel(UserEntity userEntity);

    @Named("isUserConfirmed")
    default Boolean isUserConfirmed(UserState userState) {
        return UserState.CONFIRMED.equals(userState);
    }
}
