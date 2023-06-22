package com.example.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.model.UserStatus;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest(showSql = true)
@Sql("/sql/user-repository-test-data.sql")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private static final Long ID = 1L;
    private static final String EMAIL = "dfghcvb11@naver.com";

    @Test
    void findByIdAndStatus를_통해_유저_데이터를_찾아올_수_있다() {
        // given
        // when
        Optional<UserEntity> result = userRepository.findByIdAndStatus(ID, UserStatus.ACTIVE);

        // then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void findByIdAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다() {
        // when
        Optional<UserEntity> result = userRepository.findByIdAndStatus(ID + 1, UserStatus.ACTIVE);

        // then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void findByEmailAndStatus를_통해_유저_데이터를_찾아올_수_있다() {
        // given
        // when
        Optional<UserEntity> result = userRepository.findByEmailAndStatus(EMAIL, UserStatus.ACTIVE);

        // then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void findByEmailAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다() {
        // when
        Optional<UserEntity> result = userRepository.findByEmailAndStatus(EMAIL + "Hey Yo!", UserStatus.ACTIVE);

        // then
        assertThat(result.isEmpty()).isTrue();
    }
}
