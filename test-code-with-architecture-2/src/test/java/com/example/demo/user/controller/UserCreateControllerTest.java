package com.example.demo.user.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UserCreateControllerTest {

    private static final String EMAIL = "dfghcvb11@naver.com";
    private static final String NICKNAME = "binary-ho";
    private static final String ADDRESS = "Mapo";
    private static final String UUID = "test-uuid";
    private static final long TIME_NOW = 7777777L;

    @Test
    void 사용자는_회원_가입을_할_수_있고_회원가입된_사용자는_PENDING_상태이다() {
        // Given
        TestContainer testContainer = TestContainer.builder()
            .uuidHolder(new TestUuidHolder(UUID))
            .clockHolder(new TestClockHolder(TIME_NOW))
            .build();

        UserCreate userCreate = UserCreate.builder()
            .email(EMAIL)
            .nickname(NICKNAME)
            .address(ADDRESS)
            .build();

        // When
        ResponseEntity<UserResponse> result = testContainer.userCreateController
            .createUser(userCreate);

        // Then
        assertAll(
            () -> assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED),
            () -> assertThat(result.getBody()).isNotNull(),
            () -> assertThat(result.getBody().getEmail()).isEqualTo(EMAIL),
            () -> assertThat(result.getBody().getNickname()).isEqualTo(NICKNAME),
            () -> assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.PENDING),
            () -> assertThat(result.getBody().getLastLoginAt()).isNull(),
            () -> assertThat(testContainer
                .userRepository.getById(1L).getCertificationCode())
                .isEqualTo(UUID)
        );
    }
}
