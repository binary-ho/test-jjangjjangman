package com.example.demo.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.demo.mock.FakeMailSender;
import org.junit.jupiter.api.Test;

class CertificationServiceTest {

    @Test
    void 이메일을_전송할_수_있다() {
        // Given
        FakeMailSender fakeMailSender = new FakeMailSender();
        CertificationService certificationService = new CertificationService(fakeMailSender);

        // When
        certificationService.send("dfghcvb11@naver.com", 1L, "code");

        // Then
        assertAll(
            () -> assertThat(fakeMailSender.email).isEqualTo("dfghcvb11@naver.com"),
            () -> assertThat(fakeMailSender.title).isEqualTo("Please certify your email address"),
            () -> assertThat(fakeMailSender.content).isEqualTo(
                "Please click the following link to certify your email address: http://localhost:8080/api/users/1/verify?certificationCode=code")
        );
    }
}
