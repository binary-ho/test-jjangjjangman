package com.example.demo.user.controller.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

class MyProfileResponseTest {

    private static final String EMAIL = "dfghcvb11@naver.com";
    private static final String NICKNAME = "binary-ho";
    private static final String ADDRESS = "Mapo";
    private static final UserStatus ACTIVE_STATUS = UserStatus.ACTIVE;

    @Test
    void User_객체로_MyProfileResponse_객체를_만들_수_있다() {
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
        MyProfileResponse myProfileResponse = MyProfileResponse.from(user);

        // Then
        assertAll(
            () -> assertThat(myProfileResponse.getId()).isEqualTo(user.getId()),
            () -> assertThat(myProfileResponse.getEmail()).isEqualTo(user.getEmail()),
            () -> assertThat(myProfileResponse.getAddress()).isEqualTo(user.getAddress()),
            () -> assertThat(myProfileResponse.getNickname()).isEqualTo(user.getNickname()),
            () -> assertThat(myProfileResponse.getLastLoginAt()).isEqualTo(user.getLastLoginAt()),
            () -> assertThat(myProfileResponse.getStatus()).isEqualTo(user.getStatus())
        );
    }
}
