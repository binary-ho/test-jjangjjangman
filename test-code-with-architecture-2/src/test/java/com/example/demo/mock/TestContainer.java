package com.example.demo.mock;

import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.common.service.port.UuidHolder;
import com.example.demo.post.controller.port.PostService;
import com.example.demo.post.service.PostServiceImpl;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.controller.UserController;
import com.example.demo.user.controller.UserCreateController;
import com.example.demo.user.controller.port.UserService;
import com.example.demo.user.service.CertificationService;
import com.example.demo.user.service.UserServiceImpl;
import com.example.demo.user.service.port.MailSender;
import com.example.demo.user.service.port.UserRepository;
import lombok.Builder;

public class TestContainer {

    public final MailSender mailSender;
    public final CertificationService certificationService;

    public final UserRepository userRepository;
    public final PostRepository postRepository;

    public final UserService userService;
    public final PostService postService;

    public final UserController userController;
    public final UserCreateController userCreateController;


    @Builder
    public TestContainer(ClockHolder clockHolder, UuidHolder uuidHolder) {
        this.mailSender = new FakeMailSender();
        this.certificationService = new CertificationService(mailSender);

        this.userRepository = new FakeUserRepository();
        this.postRepository = new FakePostRepository();

        this.userService = UserServiceImpl.builder()
            .uuidHolder(uuidHolder)
            .clockHolder(clockHolder)
            .certificationService(certificationService)
            .userRepository(userRepository)
            .build();

        this.postService = PostServiceImpl.builder()
            .postRepository(postRepository)
            .userService(userService)
            .build();

        this.userController = UserController.builder()
            .userService(userService)
            .build();

        this.userCreateController = new UserCreateController(userService);
    }
}
