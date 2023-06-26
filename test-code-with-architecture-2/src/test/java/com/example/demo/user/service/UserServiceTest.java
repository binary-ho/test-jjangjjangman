package com.example.demo.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserServiceTest {

    private static final String ACTIVE_USER_EMAIL = "active@naver.com";
    private static final String PENDING_USER_EMAIL = "pending@naver.com";
    private static final String ACTIVE_USER_NICKNAME = "active man";
    private static final String NICKNAME2 = "pending man";
    private static final String ADDRESS = "Mapo";
    private static final Long TIME_NOW = 123456789L;
    private static final String UUID = "this is uuid";

    private UserService userService;

    @BeforeEach
    void initUserService() {

        FakeUserRepository fakeUserRepository = new FakeUserRepository();

        this.userService = UserService.builder()
            .certificationService(new CertificationService(new FakeMailSender()))
            .clockHolder(new TestClockHolder(TIME_NOW))
            .uuidHolder(new TestUuidHolder(UUID))
            .userRepository(fakeUserRepository)
            .build();

        fakeUserRepository.save(getActiveUser());
        fakeUserRepository.save(getPendingUser());
    }

    private User getActiveUser() {
        return User.builder()
            .email(ACTIVE_USER_EMAIL)
            .nickname(ACTIVE_USER_NICKNAME)
            .address(ADDRESS)
            .status(UserStatus.ACTIVE)
            .build();
    }
    private User getPendingUser() {
        UserCreate userCreate = UserCreate.builder()
            .email(PENDING_USER_EMAIL)
            .nickname(NICKNAME2)
            .address(ADDRESS)
            .build();

        return User.from(userCreate, new TestUuidHolder(UUID));
    }

    @Test
    void getByEmail은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        // Given
        String email = ACTIVE_USER_EMAIL;

        // When
        User result = userService.getByEmail(email);

        // Then
        assertThat(result.getNickname()).isEqualTo(ACTIVE_USER_NICKNAME);
    }

    @Test
    void getByEmail은_PENDING_상태인_유저는_찾아올_수_없다() {
        // Given
        String email = PENDING_USER_EMAIL;

        // When
        // Then
        assertThatThrownBy(() -> userService.getByEmail(email))
            .isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    void getById은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        // Given
        // When
        User result = userService.getById(1L);

        // Then
        assertThat(result.getNickname()).isEqualTo(ACTIVE_USER_NICKNAME);
    }

    @Test
    void getById은_PENDING_상태인_유저는_찾아올_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> userService.getById(2L))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void UserCreate_를_이용하여_유저를_생성할_수_있다() {
        // Given
        UserCreate userCreate = UserCreate.builder()
            .email("test@naver.com")
            .address("Mapo")
            .nickname("jinhoho")
            .build();

        // When
        User result = userService.create(userCreate);

        // Then
        assertAll(
            () -> assertThat(result.getId()).isNotNull(),
            () -> assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING),
            () -> assertThat(result.getEmail()).isEqualTo("test@naver.com"),
            () -> assertThat(result.getAddress()).isEqualTo("Mapo"),
            () -> assertThat(result.getNickname()).isEqualTo("jinhoho"),
            // 이제 테스트 가능
            () -> assertThat(result.getCertificationCode()).isEqualTo(UUID)
        );
    }

    @Test
    void UserUpdate_를_이용하여_유저_정보를_수정할_수_있다() {
        // Given
        final Long ID = 1L;
        final String newAddress = "newAddress";
        final String newNickname = "newNickname";

        UserUpdate userUpdate = UserUpdate.builder()
            .nickname(newNickname)
            .address(newAddress)
            .build();

        // When
        User updateResult = userService.update(ID, userUpdate);

        // Then
        assertAll(
            () -> assertThat(updateResult.getId()).isEqualTo(ID),
            () -> assertThat(updateResult.getAddress()).isEqualTo(newAddress),
            () -> assertThat(updateResult.getNickname()).isEqualTo(newNickname)
        );
    }

    @Test
    void user를_로그인_시키면_마지막_로그인_시간이_변경된다() {
        // Given
        // When
        userService.login(1L);

        // Then
        User userEntity = userService.getById(1L);
        // 이제 테스트 가능
        assertThat(userEntity.getLastLoginAt()).isEqualTo(TIME_NOW);
    }

    @Test
    void PENDING_상태의_유저를_인증_코드로_활성화_할_수_있다() {
        // Given
        final Long ID = 2L;
        userService.verifyEmail(ID, UUID);

        // When
        User result = userService.getById(ID);

        // Then
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING_상태의_사용자는_틀린_인증_코드를_입력하면_예외를_발생시킨다() {
        // Given
        // When
        // Then
        assertAll(
            () -> assertThatThrownBy(() -> userService.verifyEmail(2L, "틀린 코드"))
                .isInstanceOf(CertificationCodeNotMatchedException.class)
        );
    }
}
