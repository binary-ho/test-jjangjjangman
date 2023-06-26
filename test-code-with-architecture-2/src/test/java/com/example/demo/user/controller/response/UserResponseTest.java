package com.example.demo.user.controller.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

class UserResponseTest {

    private static final String EMAIL = "dfghcvb11@naver.com";
    private static final String NICKNAME = "binary-ho";
    private static final String ADDRESS = "Mapo";
    private static final UserStatus ACTIVE_STATUS = UserStatus.ACTIVE;

    @Test
    void User_객체로_UserResponse_객체를_만들_수_있다() {
        // Given
        User user = User.builder()
            .id(1L)
            .email(EMAIL)
            .nickname(NICKNAME)
            .address(ADDRESS)
            .status(ACTIVE_STATUS)
            .lastLoginAt(0L)
            .build();

        // When
        UserResponse userResponse = UserResponse.from(user);

        // Then
        assertAll(
            () -> assertThat(userResponse.getId()).isEqualTo(user.getId()),
            () -> assertThat(userResponse.getEmail()).isEqualTo(user.getEmail()),
            () -> assertThat(userResponse.getNickname()).isEqualTo(user.getNickname()),
            () -> assertThat(userResponse.getLastLoginAt()).isEqualTo(user.getLastLoginAt()),
            () -> assertThat(userResponse.getStatus()).isEqualTo(user.getStatus())
        );
    }
}
