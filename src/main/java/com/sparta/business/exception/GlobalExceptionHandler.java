package com.sparta.business.exception;

import com.sparta.business.filter.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public void ExHandler(Exception e) {
        log.error(e.toString()); // 로그에 예외 정보 출력
        e.printStackTrace(); // 콘솔에 예외 스택 트레이스를 출력

        String errorMessage = e.getMessage();
        log.error("Error message: " + errorMessage);

    }

    /**
     * 예시 - 에러 세분화 작업
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException exc) {
        log.error("Illegal argument exception: " + exc.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exc.getMessage());
    }

}
