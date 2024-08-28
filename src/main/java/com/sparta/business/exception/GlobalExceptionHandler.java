package com.sparta.business.exception;

import com.sparta.business.filter.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("Unexpected error occurred: " + e.toString()); // 로그에 예외 정보 및 메세지 출력
        log.error("Error message: " + e.getMessage()); // 로그에 메시지만 출력
//        e.printStackTrace(); // 콘솔에 예외 스택 트레이스를 출력(빨간색), 기본 하얀색 에러는 인텔리제이 자체에서 반환

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    /**
     * 예시 - 에러 세분화 작업
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException exc) {
        log.error("Illegal argument exception: " + exc.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exc.getMessage());
    }

    /**
     * 접근 권한 에러 처리
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException exc) {
        log.error("Access denied: " + exc.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exc.getMessage());
    }

}
