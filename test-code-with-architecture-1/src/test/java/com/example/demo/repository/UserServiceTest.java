package com.example.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
        UserEntity result = userService.getByEmail(email);

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
        UserEntity result = userService.getById(1L);

        // Then
        assertThat(result.getNickname()).isEqualTo("binary-ho");
    }

    @Test
    void getById은_PENDING_상태인_유저는_찾아올_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> userService.getById(2L))
            .isInstanceOf(ResourceNotFoundException.class);
    }
}
