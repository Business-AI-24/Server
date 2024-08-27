package com.sparta.business.domain.master_customer.controller;

import com.sparta.business.domain.master_customer.dto.UserReviewRequestDto;
import com.sparta.business.domain.master_customer.dto.UserReviewResponseDto;
import com.sparta.business.domain.master_customer.service.UserReviewService;
import com.sparta.business.entity.User;
import com.sparta.business.entity.UserRoleEnum;
import com.sparta.business.filter.UserDetailsImpl;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/customer/reviews")
public class UserReviewController {
    private final UserReviewService reviewService;

    @PostMapping
    public ResponseEntity<UserReviewResponseDto> createReview(
        @RequestBody UserReviewRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails){
        try {
            User user = userDetails.getUser();
            if (user.getRole().equals(UserRoleEnum.OWNER)) {
                throw new AccessDeniedException("접근 권한이 없습니다.");
            }
            return ResponseEntity.ok().body(reviewService.createReview(requestDto, user));
        } catch (AccessDeniedException e) {
            throw e;
        } catch (Exception e) {
            log.error("리뷰 생성 중 오류 발생: ", e);
            throw new RuntimeException("리뷰 생성 중 오류가 발생했습니다.");
        }
    }

    @PutMapping("/{review_id}")
    public ResponseEntity<UserReviewResponseDto> updateReview(
        @PathVariable("review_id") UUID id,
        @RequestBody UserReviewRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails){
        try {
            User user = userDetails.getUser();
            if (user.getRole().equals(UserRoleEnum.OWNER)) {
                throw new AccessDeniedException("접근 권한이 없습니다.");
            }
            return ResponseEntity.ok().body(reviewService.updateReview(id, requestDto, user));
        } catch (AccessDeniedException e) {
            throw e;
        } catch (Exception e) {
            log.error("리뷰 수정 중 오류 발생: ", e);
            throw new RuntimeException("리뷰 수정 중 오류가 발생했습니다.");
        }
    }

    @DeleteMapping("/{review_id}")
    public ResponseEntity<UserReviewResponseDto> deleteReview(
        @PathVariable("review_id") UUID id,
        @AuthenticationPrincipal UserDetailsImpl userDetails){
        try {
            User user = userDetails.getUser();
            if (user.getRole().equals(UserRoleEnum.OWNER)) {
                throw new AccessDeniedException("접근 권한이 없습니다.");
            }
            return ResponseEntity.ok().body(reviewService.deleteReview(id, user));
        } catch (AccessDeniedException e) {
            throw e;
        } catch (Exception e) {
            log.error("리뷰 삭제 중 오류 발생: ", e);
            throw new RuntimeException("리뷰 삭제 중 오류가 발생했습니다.");
        }
    }


}
