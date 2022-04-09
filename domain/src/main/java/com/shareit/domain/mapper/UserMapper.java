package com.shareit.domain.mapper;

import com.shareit.domain.UserEntity;
import com.shareit.domain.dto.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserMapper {
    static final UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    User toModel(UserEntity userEntity);
    UserEntity toEntity(User model);

}
