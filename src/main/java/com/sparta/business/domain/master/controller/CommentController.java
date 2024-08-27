package com.sparta.business.domain.master.controller;

import com.sparta.business.domain.master.dto.CommentRequestDto;
import com.sparta.business.domain.master.dto.CommentResponseDto;
import com.sparta.business.domain.master.dto.NoticeRequestDto;
import com.sparta.business.domain.master.dto.NoticeResponseDto;
import com.sparta.business.domain.master.service.CommentService;
import com.sparta.business.domain.master.service.NoticeService;
import com.sparta.business.entity.User;
import com.sparta.business.entity.UserRoleEnum;
import com.sparta.business.filter.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/comment")
public class CommentController {

    /**
     * 모든 도메인의 컨트롤러 끝단은 접근 권한 및 로그인 체크가 진행되어야 합니다.
     */
    private final CommentService commentService;


    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails){
        try {
            User user = userDetails.getUser();
            if (!user.getRole().equals(UserRoleEnum.MASTER)) {
                throw new AccessDeniedException("접근 권한이 없습니다.");
            }
            return ResponseEntity.ok().body(commentService.createComment(requestDto, user));
        } catch (AccessDeniedException e) {
            throw e;
        } catch (Exception e) {
            log.error("컴플레인의 답글 생성 중 오류 발생: ", e);
            throw new RuntimeException("컴플레인의 답글 생성 중 오류가 발생했습니다.");
        }
    }

    @PutMapping("/{comment_id}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable("comment_id") String id,
        @RequestBody CommentRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails){
        try {
            User user = userDetails.getUser();
            if (!user.getRole().equals(UserRoleEnum.MASTER)) {
                throw new AccessDeniedException("접근 권한이 없습니다.");
            }
            return ResponseEntity.ok().body(commentService.updateComment(id, requestDto));
        } catch (AccessDeniedException e) {
            throw e;
        } catch (Exception e) {
            log.error("컴플레인에 대한 답글 수정 중 오류 발생: ", e);
            throw new RuntimeException("컴플레인에 대한 수정 중 오류가 발생했습니다.");
        }
    }

    @DeleteMapping("/{comment_id}")
    public ResponseEntity<CommentResponseDto> deleteNotice(@PathVariable("comment_id") String id,
        @AuthenticationPrincipal UserDetailsImpl userDetails){
        try {
            User user = userDetails.getUser();
            if (!user.getRole().equals(UserRoleEnum.MASTER)) {
                throw new AccessDeniedException("접근 권한이 없습니다.");
            }
            return ResponseEntity.ok().body(commentService.deleteComment(id));
        } catch (AccessDeniedException e) {
            throw e;
        } catch (Exception e) {
            log.error("컴플레인에 대한 답글 삭제 중 오류 발생: ", e);
            throw new RuntimeException("컴플레인에 대한 답글 삭제 중 오류가 발생했습니다.");
        }
    }
}
