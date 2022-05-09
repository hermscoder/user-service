package com.shareit.service;

import com.shareit.data.repository.UserRepository;
import com.shareit.domain.dto.User;
import com.shareit.domain.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class ContextService implements AuthService {
    private final UserRepository userRepository;

    public ContextService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //TODO get the REAL current user, from the token.
    @Override
    public User getCurrentUser() {
        return UserMapper.INSTANCE.toModel(userRepository.getById(1L), null);
    }
}
