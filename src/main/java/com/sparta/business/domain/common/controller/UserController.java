package com.sparta.business.domain.common.controller;

import com.sparta.business.domain.common.dto.SignUpRequestDto;
import com.sparta.business.domain.common.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/common")
public class UserController {

    private final UserService userService;

    @PostMapping("/sign_up")
    public ResponseEntity<String> signup(@Valid @RequestBody SignUpRequestDto requestDto) {
        userService.signup(requestDto);
        return ResponseEntity.ok().body("회원 가입이 완료되었습니다.");
    }
}
