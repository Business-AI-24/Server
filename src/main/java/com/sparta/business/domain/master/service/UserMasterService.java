package com.sparta.business.domain.master.service;

import com.sparta.business.domain.common.repository.UserRepository;
import com.sparta.business.domain.master.dto.UserMasterRequestDto;
import com.sparta.business.entity.User;
import com.sparta.business.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserMasterService {

    private final UserRepository userRepository;

    @Transactional
    @CacheEvict(cacheNames = "userDetailsCache", key = "args[0]")
    public String updateUserRole(String username, UserMasterRequestDto requestDto) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
            new IllegalArgumentException("Not Found " + username)
        );

        user.setRole(UserRoleEnum.valueOf(requestDto.getRole()));
        userRepository.save(user);

        return "User role updated to " + user.getRole().name();
    }
}
