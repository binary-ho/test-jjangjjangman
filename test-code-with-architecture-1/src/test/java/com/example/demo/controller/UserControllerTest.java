package com.example.demo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.model.UserStatus;
import com.example.demo.model.dto.UserUpdateDto;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
    @Sql(value = "/sql/user-controller-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(value = "/sql/delete-all-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
})
public class UserControllerTest {

    private static final Long ID = 1L;
    private static final String EMAIL = "active@naver.com";
    private static final String NICKNAME = "binary-ho";
    private static final String ADDRESS = "Mapo";
    private static final UserStatus STATUS = UserStatus.ACTIVE;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // TODO : 중요
    @Test
    void 사용자는_주소를_제외한_유저의_정보를_전달_받을_수_있다() throws Exception {
        // Given
        // When
        // Then
        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(ID))
            .andExpect(jsonPath("$.email").value(EMAIL))
            .andExpect(jsonPath("$.nickname").value(NICKNAME))
            .andExpect(jsonPath("$.address").doesNotExist())
            .andExpect(jsonPath("$.status").value(STATUS.toString()));
    }

    @Test
    void 사용자는_존재하지_않는_유저의_아이디로_API_를_호출할_경우_404_응답을_받는다() throws Exception {
        // Given
        Long wrongId = 987654321L;
        String errorMessage = "Users에서 ID %d를 찾을 수 없습니다.";
        // When
        // Then

        mockMvc.perform(get(String.format("/api/users/%d", wrongId)))
            .andExpect(status().isNotFound())
            .andExpect(content().string(String.format(errorMessage, wrongId)));
    }

    @Test
    void 사용자는_인증_코드로_계정을_활성화_시킬_수_있다() {
        // Given
        Long id = 2L;
        // When
        // Then
        assertAll(
            () -> mockMvc.perform(
                    get(String.format("/api/users/%d/verify", id))
                        .queryParam("certificationCode", "aaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                .andExpect(status().isFound()),
            () -> assertThat(
                userRepository.findById(id).get().getStatus()).isEqualTo(UserStatus.ACTIVE)
        );
    }

    @Test
    void 사용자는_인증_코드가_잘못된_경우_FORBIDDEN_응답을_받는다() throws Exception {
        // Given
        Long id = 2L;
        // When
        // Then
        mockMvc.perform(
                get(String.format("/api/users/%d/verify", id))
                    .queryParam("certificationCode", "wrong code"))
            .andExpect(status().isForbidden());
    }

    @Test
    void 인증_코드_인증_API_호출을_없는_유저에게_보내는_경우_404_응답을_받는다() throws Exception {
        // Given
        Long wrongId = 98765L;
        // When
        // Then
        mockMvc.perform(
                get(String.format("/api/users/me"))
                    .queryParam("certificationCode", "aaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
            .andExpect(status().isNotFound());
    }

    @Test
    void 이메일을_통해_주소를_포함한_내_정보를_가져올_수_있다() throws Exception {
        // Given
        // When
        // Then
        mockMvc.perform(get(String.format("/api/users/me"))
                .header("EMAIL", EMAIL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(ID))
            .andExpect(jsonPath("$.email").value(EMAIL))
            .andExpect(jsonPath("$.nickname").value(NICKNAME))
            .andExpect(jsonPath("$.address").value(ADDRESS))
            .andExpect(jsonPath("$.status").value(STATUS.toString()));
        // FIXME .andExpect(jsonPath("$.lastLoginAt") -> 테스트 할 수 없다. ㅠ.ㅠ
    }

    // TODO : 중요
    @Test
    void 내_정보를_업데이트_할_수_있다() throws Exception {
        // Given
        final String NEW_NICKNAME = "이진호";
        final String NEW_ADDRESS = "아크로 서울 포레스트";
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
            .nickname(NEW_NICKNAME)
            .address(NEW_ADDRESS)
            .build();

        // When
        // Then
        mockMvc.perform(put(String.format("/api/users/me"))
                .header("EMAIL", EMAIL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userUpdateDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(ID))
            .andExpect(jsonPath("$.email").value(EMAIL))
            .andExpect(jsonPath("$.nickname").value(NEW_NICKNAME))
            .andExpect(jsonPath("$.address").value(NEW_ADDRESS))
            .andExpect(jsonPath("$.status").value(STATUS.toString()));
        // FIXME .andExpect(jsonPath("$.lastLoginAt") -> 테스트 할 수 없다. ㅠ.ㅠ
    }
}
