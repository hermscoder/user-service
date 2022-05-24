package com.shareit.service;

import com.shareit.data.repository.UserRepository;
import com.shareit.domain.dto.Media;
import com.shareit.domain.entity.UserEntity;
import com.shareit.domain.dto.registration.UserRegistration;
import com.shareit.domain.dto.User;
import com.shareit.domain.entity.UserState;
import com.shareit.domain.mapper.UserMapper;
import com.shareit.service.client.MediaClient;
import com.shareit.utils.commons.exception.InvalidParameterException;
import com.shareit.exception.UserNotFoundException;
import com.shareit.infrastructure.cryptography.Encrypter;
import com.shareit.utils.validator.EmailValidator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmailValidator emailValidator;
    private final Encrypter encrypter;
    private final MediaClient mediaClient;
    private final AuthService authService;

    public UserService(UserRepository userRepository, EmailValidator emailValidator, Encrypter encrypter, MediaClient mediaClient, AuthService authService) {
        this.userRepository = userRepository;
        this.emailValidator = emailValidator;
        this.encrypter = encrypter;
        this.mediaClient = mediaClient;
        this.authService = authService;
    }

    public User getUserById(Long userId) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()){
            throw new UserNotFoundException(userId);
        }
        UserEntity userEntity = userOptional.get();

        Media userMedia = null;
        if(Objects.nonNull(userEntity.getProfilePictureId())) {
            userMedia = mediaClient.getMediaById(userEntity.getProfilePictureId());
        }

        User user = UserMapper.INSTANCE.toModel(userEntity, userMedia);

        return user;
    }

    public UserEntity signUpUser(UserRegistration userRegistration) {
        if(!emailValidator.isValid(userRegistration.getEmail())) {
            throw new InvalidParameterException("email");
        }

        if(!userRegistration.getPassword().equals(userRegistration.getPasswordConfirmation())) {
            throw new InvalidParameterException("passwordConfirmation");
        }

        userRegistration.setPassword(encrypter.encrypt(userRegistration.getPassword()));
        UserEntity userEntity = userRepository.save(UserMapper.INSTANCE.toEntity(userRegistration));

        return userEntity;
    }



    public int enableAppUser(Long userId) {
        return userRepository.changeUserState(UserState.CONFIRMED, userId);
    }

    public void uploadProfilePicture(MultipartFile file) {
        User currentUser = authService.getCurrentUser();

        Media media;
        if(currentUser.getProfilePicture() != null) {
            media = mediaClient.updateMedia(currentUser.getProfilePicture().getId(), file);
        } else {
            media = mediaClient.createMedias(new MultipartFile[] { file } ).get(0);
        }
        userRepository.changeUserProfileImage(currentUser.getId(), media.getId());
    }


}
