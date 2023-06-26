package com.example.demo.post;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

public class PostTest {

    @Test
    void PostCreate_와_User_로_게시물을_만들_수_있다() {
        // Given
        PostCreate postCreate = PostCreate.builder()
            .writerId(1L)
            .content("content content")
            .build();

        User writer = User.builder()
            .id(1L)
            .email("dfghcvb11@naver.com")
            .nickname("binary-ho")
            .address("Mapo")
            .certificationCode("code-code")
            .status(UserStatus.ACTIVE)
            .lastLoginAt(0L)
            .build();

        // When
        Post post = Post.from(writer, postCreate);

        // Then
        assertAll(
            () -> assertThat(post.getContent()).isEqualTo(postCreate.getContent()),
            () -> assertThat(post.getWriter()).isEqualTo(writer),
            () -> assertThat(post.getWriter().getId()).isEqualTo(writer.getId()),
            () -> assertThat(post.getWriter().getEmail()).isEqualTo(writer.getEmail()),
            () -> assertThat(post.getWriter().getAddress()).isEqualTo(writer.getAddress()),
            () -> assertThat(post.getWriter().getNickname()).isEqualTo(writer.getNickname()),
            () -> assertThat(post.getWriter().getCertificationCode()).isEqualTo(writer.getCertificationCode()),
            () -> assertThat(post.getWriter().getLastLoginAt()).isEqualTo(writer.getLastLoginAt()),
            () -> assertThat(post.getWriter().getStatus()).isEqualTo(writer.getStatus())
        );
    }

    @Test
    void PostUpdate_로_Post_정보를_갱신할_수_있다() {
        // Given
        Post post = Post.builder()
            .content("before update")
            .build();

        final String NEW_CONTENT = "after update";
        PostUpdate postUpdate = PostUpdate.builder()
            .content(NEW_CONTENT)
            .build();

        // When
        Post postAfterUpdate = post.update(postUpdate);

        // Then
        assertThat(postAfterUpdate.getContent()).isEqualTo(NEW_CONTENT);
    }
}
