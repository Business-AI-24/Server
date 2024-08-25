package com.sparta.business.domain.common.service;

import com.sparta.business.domain.common.dto.SignUpRequestDto;
import com.sparta.business.domain.common.repository.UserRepository;
import com.sparta.business.entity.User;
import com.sparta.business.entity.UserRoleEnum;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public void signup(SignUpRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // nickName 중복확인
        String nickname = requestDto.getNickname();
        Optional<User> checkEmail = userRepository.findByNickname(nickname);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 닉네임 입니다.");
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.valueOf(requestDto.getType());
        if (role.equals(UserRoleEnum.MASTER)) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.MASTER;
        }

        // 사용자 등록
        userRepository.save(new User(requestDto, password, role));
    }
}
