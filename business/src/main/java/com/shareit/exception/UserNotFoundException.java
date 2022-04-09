package com.shareit.exception;

public class UserNotFoundException extends BadRequestException {
    private Long userId;

    public UserNotFoundException(Long userId) {
        super(String.format("User not found: %s", userId));
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
