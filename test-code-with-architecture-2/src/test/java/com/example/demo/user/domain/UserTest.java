package com.example.demo.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import org.junit.jupiter.api.Test;

public class UserTest {

    private static final String EMAIL = "dfghcvb11@naver.com";
    private static final String NICKNAME = "binary-ho";
    private static final String ADDRESS = "Mapo";
    private static final String UUID = "uuuuuuuuid";
    private static final long TIME_NOW = 7777777L;

    @Test
    void User는_UserCreate_객체로_생성할_수_있다() {
        // Given
        UserCreate userCreate = UserCreate.builder()
            .email(EMAIL)
            .nickname(NICKNAME)
            .address(ADDRESS)
            .build();

        // When
        User user = User.from(userCreate, new TestUuidHolder(UUID));

        // Then
        assertAll(
            () -> assertThat(user.getId()).isEqualTo(null),
            () -> assertThat(user.getEmail()).isEqualTo(EMAIL),
            () -> assertThat(user.getNickname()).isEqualTo(NICKNAME),
            () -> assertThat(user.getAddress()).isEqualTo(ADDRESS),
            () -> assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING),
            () -> assertThat(user.getCertificationCode()).isEqualTo(UUID)
        );
    }

    @Test
    void User는_UserUpdate_객체로_데이터를_업데이트_할_수_있다() {
        // Given

        // When

        // Then
    }

    @Test
    void 로그인을_할_수_있고_로그인시_마지막_로그인_시간이_변경된다() {
        // Given
        User user = User.builder()
            .id(1L)
            .email(EMAIL)
            .nickname(NICKNAME)
            .address(ADDRESS)
            .status(UserStatus.ACTIVE)
            .certificationCode(UUID)
            .lastLoginAt(0L)
            .build();

        // When
        User userAfterLogin = user.login(new TestClockHolder(TIME_NOW));

        // Then
        assertThat(userAfterLogin.getLastLoginAt()).isEqualTo(TIME_NOW);
    }
}
