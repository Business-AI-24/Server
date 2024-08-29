package com.sparta.business.domain.open_api;

import com.sparta.business.entity.AIQuestion;
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
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/owner/ask")
public class AIQuestionController {

    private final AIQuestionService aiQuestionService;

    /**
     * 권한은 OWNER 가 상품을 등록할 때 호출 되도록 진행
     * @param requestDto
     * @return
     */
    @PostMapping("/description")
    public Mono<ResponseEntity<String>> createProductDescription(@RequestBody AiRequestDto requestDto,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        try {
            User user = userDetails.getUser();
            if (user.getRole().equals(UserRoleEnum.CUSTOMER)) {
                throw new AccessDeniedException("접근 권한이 없습니다.");
            }
            return aiQuestionService.createProductDescription(requestDto).map(ResponseEntity::ok);
        } catch (AccessDeniedException e) {
            throw e;
        } catch (Exception e) {
            log.error("컴플레인의 답글 생성 중 오류 발생: ", e);
            throw new RuntimeException("컴플레인의 답글 생성 중 오류가 발생했습니다.");
        }
    }

}
