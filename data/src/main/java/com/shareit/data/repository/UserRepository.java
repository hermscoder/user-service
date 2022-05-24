package com.shareit.data.repository;

import com.shareit.domain.entity.UserEntity;
import com.shareit.domain.entity.UserState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE UserEntity a " +
            "SET a.state = ?1  WHERE a.id = ?2")
    int changeUserState(UserState confirmed, Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity a " +
            "SET a.profilePictureId = ?2  WHERE a.id = ?1")
    int changeUserProfileImage(Long userId, Long mediaId);
}
