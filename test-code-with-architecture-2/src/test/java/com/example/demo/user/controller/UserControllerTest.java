package com.example.demo.user.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.response.MyProfileResponse;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.service.port.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UserControllerTest {

    private static final Long ID = 1L;
    private static final String EMAIL = "active@naver.com";
    private static final String NICKNAME = "binary-ho";
    private static final String ADDRESS = "Mapo";
    private static final String UUID = "test-uuid";
    private static final long TIME_NOW = 7777777L;

    // TODO : 중요
    @Test
    void 사용자는_주소를_제외한_유저의_정보를_전달_받을_수_있다() {
        // Given
        TestContainer testContainer = TestContainer.builder().build();
        UserRepository userRepository = testContainer.userRepository;

        User user = User.builder()
            .id(1L)
            .email(EMAIL)
            .nickname(NICKNAME)
            .address(ADDRESS)
            .status(UserStatus.ACTIVE)
            .certificationCode(UUID)
            .lastLoginAt(0L)
            .build();

        userRepository.save(user);

        // When
        ResponseEntity<UserResponse> result = testContainer.userController.getUserById(1L);

        // Then
        assertAll(
            () -> assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK),
            () -> assertThat(result.getBody()).isNotNull(),
            () -> assertThat(result.getBody().getEmail()).isEqualTo(EMAIL),
            () -> assertThat(result.getBody().getNickname()).isEqualTo(NICKNAME),
            () -> assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE),
            () -> assertThat(result.getBody().getLastLoginAt()).isEqualTo(0L)
        );
    }

    @Test
    void 사용자는_존재하지_않는_유저의_아이디로_API_를_호출할_경우_404_응답을_받는다() {
        // Given
        TestContainer testContainer = TestContainer.builder().build();

        // When
        // Then
        assertThatThrownBy(
            () -> testContainer.userController.getUserById(1L))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 사용자는_인증_코드로_계정을_활성화_시킬_수_있다() {
        // Given
        TestContainer testContainer = TestContainer.builder().build();

        User user = User.builder()
            .id(1L)
            .email(EMAIL)
            .nickname(NICKNAME)
            .address(ADDRESS)
            .status(UserStatus.PENDING)
            .certificationCode(UUID)
            .lastLoginAt(0L)
            .build();

        testContainer.userRepository.save(user);

        // When
        ResponseEntity<Void> result = testContainer.userController
            .verifyEmail(1L, UUID);

        // Then
        assertAll(
            () -> assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FOUND),
            () -> assertThat(
                testContainer.userRepository.getById(1L).getStatus())
                .isEqualTo(UserStatus.ACTIVE)
        );
    }

    @Test
    void 사용자는_인증_코드가_잘못된_경우_FORBIDDEN_응답을_받는다() {
        // Given
        TestContainer testContainer = TestContainer.builder().build();

        User user = User.builder()
            .id(1L)
            .email(EMAIL)
            .nickname(NICKNAME)
            .address(ADDRESS)
            .status(UserStatus.PENDING)
            .certificationCode(UUID)
            .lastLoginAt(0L)
            .build();

        testContainer.userRepository.save(user);

        // When
        // Then
        assertThatThrownBy(
            () -> testContainer.userController.verifyEmail(1L, UUID + "Wrong Code"))
            .isInstanceOf(CertificationCodeNotMatchedException.class);
    }

    @Test
    void 인증_코드_인증_API_호출을_없는_유저에게_보내는_경우_404_응답을_받는다() {
        // Given
        TestContainer testContainer = TestContainer.builder().build();

        // When
        // Then
        assertThatThrownBy(
            () -> testContainer.userController.verifyEmail(1L, UUID))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 이메일을_통해_주소를_포함한_내_정보를_가져올_수_있다() {
        // Given
        TestContainer testContainer = TestContainer.builder()
            .clockHolder(new TestClockHolder(TIME_NOW))
            .build();

        User user = User.builder()
            .id(1L)
            .email(EMAIL)
            .nickname(NICKNAME)
            .address(ADDRESS)
            .status(UserStatus.ACTIVE)
            .certificationCode(UUID)
            .lastLoginAt(0L)
            .build();

        testContainer.userRepository.save(user);

        // When
        ResponseEntity<MyProfileResponse> result = testContainer.userController
            .getMyInfo(EMAIL);

        // Then
        assertAll(
            () -> assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK),
            () -> assertThat(result.getBody()).isNotNull(),
            () -> assertThat(result.getBody().getAddress()).isEqualTo(ADDRESS),

            () -> assertThat(result.getBody().getEmail()).isEqualTo(EMAIL),
            () -> assertThat(result.getBody().getNickname()).isEqualTo(NICKNAME),
            () -> assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE),
            () -> assertThat(result.getBody().getLastLoginAt()).isEqualTo(TIME_NOW)
        );
    }

    // TODO : 중요
    @Test
    void 사용자는_자신의_정보를_업데이트_할_수_있다() {
        // Given
        TestContainer testContainer = TestContainer.builder().build();

        User user = User.builder()
            .id(1L)
            .email(EMAIL)
            .nickname(NICKNAME)
            .address(ADDRESS)
            .status(UserStatus.ACTIVE)
            .certificationCode(UUID)
            .lastLoginAt(0L)
            .build();


        testContainer.userRepository.save(user);

        final String NEW_NICKNAME = "이진호";
        final String NEW_ADDRESS = "아크로 서울 포레스트";
        UserUpdate userUpdate = UserUpdate.builder()
            .nickname(NEW_NICKNAME)
            .address(NEW_ADDRESS)
            .build();

        // When
        ResponseEntity<MyProfileResponse> result = testContainer.userController
            .updateMyInfo(EMAIL, userUpdate);

        // Then
        // Then
        assertAll(
            () -> assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK),
            () -> assertThat(result.getBody()).isNotNull(),
            () -> assertThat(result.getBody().getAddress()).isEqualTo(NEW_ADDRESS),
            () -> assertThat(result.getBody().getNickname()).isEqualTo(NEW_NICKNAME),

            () -> assertThat(result.getBody().getEmail()).isEqualTo(EMAIL),
            () -> assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE),
            () -> assertThat(result.getBody().getLastLoginAt()).isEqualTo(0L)
        );
    }
}
