package com.sparta.business.domain.master.controller;

import com.sparta.business.domain.master.dto.NoticeRequestDto;
import com.sparta.business.domain.master.dto.NoticeResponseDto;
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
@RequestMapping("/admin/notice")
public class NoticeController {

    /**
     * 모든 도메인의 컨트롤러 끝단은 접근 권한 및 로그인 체크가 진행되어야 합니다.
     */
    private final NoticeService noticeService;

    /**
     * 공지사항 생성 메서드
     * @param requestDto
     * @param userDetails
     * @return
     */
    @PostMapping
    public ResponseEntity<NoticeResponseDto> createNotice(@RequestBody NoticeRequestDto requestDto,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        try {
            User user = userDetails.getUser();
            if (!user.getRole().equals(UserRoleEnum.MASTER)) {
                throw new AccessDeniedException("접근 권한이 없습니다.");
            }
            return ResponseEntity.ok().body(noticeService.createNotice(requestDto, user));
        } catch (AccessDeniedException e) {
            throw e;
        } catch (Exception e) {
            log.error("공지사항 생성 중 오류 발생: ", e);
            throw new RuntimeException("공지사항 생성 중 오류가 발생했습니다.");
        }
    }

    /**
     * 공지사항 수정 메서드
     * 숨김 처리 가능
     * @param id
     * @param requestDto
     * @param userDetails
     * @return
     */

    @PutMapping("/{notice_id}")
    public ResponseEntity<NoticeResponseDto> updateNotice(@PathVariable("notice_id") String id,
                                                        @RequestBody NoticeRequestDto requestDto,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        try {
            User user = userDetails.getUser();
            if (!user.getRole().equals(UserRoleEnum.MASTER)) {
                throw new AccessDeniedException("접근 권한이 없습니다.");
            }
            return ResponseEntity.ok().body(noticeService.updateNotice(id, requestDto));
        } catch (AccessDeniedException e) {
            throw e;
        } catch (Exception e) {
            log.error("공지사항 수정 중 오류 발생: ", e);
            throw new RuntimeException("공지사항 수정 중 오류가 발생했습니다.");
        }
    }

    /**
     * 공지사항 삭제 메서드
     * Soft Delete 방식으로 진행
     * @param id
     * @param userDetails
     * @return
     */
    @DeleteMapping("/{notice_id}")
    public ResponseEntity<NoticeResponseDto> deleteNotice(@PathVariable("notice_id") String id,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        try {
            User user = userDetails.getUser();
            if (!user.getRole().equals(UserRoleEnum.MASTER)) {
                throw new AccessDeniedException("접근 권한이 없습니다.");
            }
            return ResponseEntity.ok().body(noticeService.deleteNotice(id));
        } catch (AccessDeniedException e) {
            throw e;
        } catch (Exception e) {
            log.error("공지사항 삭제 중 오류 발생: ", e);
            throw new RuntimeException("공지사항 삭제 중 오류가 발생했습니다.");
        }
    }

}
