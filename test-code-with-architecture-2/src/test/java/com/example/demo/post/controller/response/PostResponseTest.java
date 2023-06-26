package com.example.demo.post.controller.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

class PostResponseTest {

    @Test
    void Post_로_응답을_생성할_수_있다() {
        // Given

        final String content = "hello world";
        final String email = "dfghcvb11@naver.com";
        final String nickname = "binary-ho";

        User writer = User.builder()
            .id(1L)
            .email(email)
            .nickname(nickname)
            .address("Mapo")
            .certificationCode("code-code")
            .status(UserStatus.ACTIVE)
            .lastLoginAt(0L)
            .build();

        Post post = Post.builder()
            .content(content)
            .writer(writer)
            .build();

        // When
        PostResponse postResponse = PostResponse.from(post);

        // Then
        assertAll(
            () -> assertThat(postResponse.getContent()).isEqualTo(content),
            () -> assertThat(postResponse.getWriter().getEmail()).isEqualTo(email),
            () -> assertThat(postResponse.getWriter().getNickname()).isEqualTo(nickname),
            () -> assertThat(postResponse.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE)
        );
    }
}
