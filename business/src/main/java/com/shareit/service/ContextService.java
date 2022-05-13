package com.shareit.service;

import com.shareit.data.repository.UserRepository;
import com.shareit.domain.dto.User;
import com.shareit.domain.entity.UserEntity;
import com.shareit.domain.mapper.UserMapper;
import com.shareit.service.client.MediaClient;
import org.springframework.stereotype.Service;

@Service
public class ContextService implements AuthService {
    private final UserRepository userRepository;
    private final MediaClient mediaClient;

    public ContextService(UserRepository userRepository, MediaClient mediaClient) {
        this.userRepository = userRepository;
        this.mediaClient = mediaClient;
    }

    //TODO get the REAL current user, from the token.
    @Override
    public User getCurrentUser() {
        UserEntity userEntity = userRepository.getById(1L);
        return UserMapper.INSTANCE.toModel(userEntity, mediaClient.getMediaById(userEntity.getProfilePictureId()));
    }
}
