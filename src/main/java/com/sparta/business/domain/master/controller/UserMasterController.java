package com.sparta.business.domain.master.controller;

import com.sparta.business.domain.master.dto.CommentRequestDto;
import com.sparta.business.domain.master.dto.CommentResponseDto;
import com.sparta.business.domain.master.dto.UserMasterRequestDto;
import com.sparta.business.domain.master.service.UserMasterService;
import com.sparta.business.entity.User;
import com.sparta.business.entity.UserRoleEnum;
import com.sparta.business.filter.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/user")
public class UserMasterController {

    private final UserMasterService userMasterService;

    /**
     * User 의 role 권한 변경 api
     */
    @PutMapping("/{username}")
    public ResponseEntity<String> updateUserRole(
        @PathVariable("username") String username,
        @RequestBody UserMasterRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails){
        try {
            User user = userDetails.getUser();
            log.info("userRole : " + user.getRole());
            log.info("username : " + user.getUsername());
            if (!user.getRole().equals(UserRoleEnum.MASTER)) {
                throw new AccessDeniedException("접근 권한이 없습니다.");
            }
            return ResponseEntity.ok().body(userMasterService.updateUserRole(username, requestDto));
        } catch (AccessDeniedException e) {
            throw e;
        } catch (Exception e) {
            log.error("유저의 역할 변경 중 생성 중 오류 발생: ", e);
            throw new RuntimeException("유저의 역할 변경 중 오류가 발생했습니다.");
        }
    }
}
