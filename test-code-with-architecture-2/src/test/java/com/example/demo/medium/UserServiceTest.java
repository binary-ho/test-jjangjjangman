package com.example.demo.medium;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;

@SpringBootTest
@SqlGroup({
    @Sql(value = "/sql/user-service-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(value = "/sql/delete-all-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
})
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void getByEmail은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        // Given
        String email = "active@naver.com";

        // When
        User result = userService.getByEmail(email);

        // Then
        assertThat(result.getNickname()).isEqualTo("binary-ho");
    }

    @Test
    void getByEmail은_PENDING_상태인_유저는_찾아올_수_없다() {
        // Given
        String email = "pending@naver.com";

        // When
        // Then
        assertThatThrownBy(() -> userService.getByEmail(email))
            .isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    void getById은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        // Given
        // When
        User result = userService.getById(2L);

        // Then
        assertThat(result.getNickname()).isEqualTo("binary-ho");
    }

    @Test
    void getById은_PENDING_상태인_유저는_찾아올_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> userService.getById(3L))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @MockBean
    private JavaMailSender mailSender;

    @Test
    void UserCreateDto_를_이용하여_유저를_생성할_수_있다() {
        // Given
        UserCreate userCreate = UserCreate.builder()
            .email("test@naver.com")
            .address("Mapo")
            .nickname("jinhoho")
            .build();
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // When
        User result = userService.create(userCreate);

        // Then
        assertAll(
            () -> assertThat(result.getId()).isNotNull(),
            () -> assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING),
            () -> assertThat(result.getEmail()).isEqualTo("test@naver.com"),
            () -> assertThat(result.getAddress()).isEqualTo("Mapo"),
            () -> assertThat(result.getNickname()).isEqualTo("jinhoho")
        );
        // FIXME : assertThat(result.getCertificationCode()).isEqualTo(헉!! 테스트 불가능!!)
    }

    @Test
    void UserUpdateDto_를_이용하여_유저_정보를_수정할_수_있다() {
        // Given
        final Long ID = 2L;
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
        userService.login(2L);

        // Then
        User userEntity = userService.getById(2L);
        assertThat(userEntity.getLastLoginAt()).isGreaterThan(0L);
        // FIXME : assertThat(result.getLastLoginAt()).isEqualTo(헉!! 테스트 불가능!!)
    }

    @Test
    void PENDING_상태의_유저를_인증_코드로_활성화_할_수_있다() {
        // Given
        final Long ID = 3L;
        userService.verifyEmail(ID, "aaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

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
            () -> assertThatThrownBy(() -> userService.verifyEmail(3L, "틀린 코드"))
                .isInstanceOf(CertificationCodeNotMatchedException.class)
        );
    }
}
