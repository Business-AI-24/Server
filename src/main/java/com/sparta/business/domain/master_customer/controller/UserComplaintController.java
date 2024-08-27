package com.sparta.business.domain.master_customer.controller;

import com.sparta.business.domain.master_customer.dto.UserComplaintsRequestDto;
import com.sparta.business.domain.master_customer.dto.UserComplaintResponseDto;
import com.sparta.business.domain.master_customer.service.UserComplaintService;
import com.sparta.business.entity.User;
import com.sparta.business.entity.UserRoleEnum;
import com.sparta.business.filter.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/customer/complaints")
public class UserComplaintController {
    private final UserComplaintService userComplaintService;

    /**
     * 해당 Store 와 Complaint 를 1대 다의 관계를 맺어서 저장해주는 방식 추후에 고려
     * @param requestDto
     * @param userDetails
     * @return
     */
    @PostMapping
    public ResponseEntity<UserComplaintResponseDto> createComplaint(@RequestBody UserComplaintsRequestDto requestDto,
                                                                    @AuthenticationPrincipal UserDetailsImpl userDetails){
        try {
            User user = userDetails.getUser();
            if (user.getRole().equals(UserRoleEnum.OWNER)) {
                throw new AccessDeniedException("접근 권한이 없습니다.");
            }
            return ResponseEntity.ok().body(userComplaintService.createComplaint(requestDto, user));
        } catch (AccessDeniedException e) {
            throw e;
        } catch (Exception e) {
            log.error("컴플레인의 생성 중 오류 발생: ", e);
            throw new RuntimeException("컴플레인의 생성 중 오류가 발생했습니다.");
        }
    }

}
