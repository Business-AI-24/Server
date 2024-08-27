package com.sparta.business.domain.master_customer.controller;

import com.sparta.business.domain.master_customer.dto.UserCommentResponseDto;
import com.sparta.business.domain.master_customer.service.UserCommentService;
import com.sparta.business.entity.User;
import com.sparta.business.entity.UserRoleEnum;
import com.sparta.business.filter.UserDetailsImpl;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/customer/comments")
public class UserCommentController {

    private final UserCommentService userCommentService;

    @GetMapping("/{complaint_id}")
    public ResponseEntity<UserCommentResponseDto> getComment(@PathVariable("complaint_id") UUID id,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails){
        try{
            User user = userDetails.getUser();
            if (user.getRole().equals(UserRoleEnum.OWNER)) {
                throw new AccessDeniedException("접근 권한이 없습니다.");
            }
            return ResponseEntity.ok().body(userCommentService.getComment(id));

        }catch (AccessDeniedException e) {
            throw e;
        } catch (Exception e) {
            log.error("컴플레인 답글 조회중 오류 발생: ", e);
            throw new RuntimeException("컴플레인 답글 조회중 오류가 발생했습니다.");
        }
    }
}
