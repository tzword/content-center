package com.tzword.contentcenter.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author jianghy
 * @Description:
 * @date 2021/4/20 11:14
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionErrorHandler {
    @ExceptionHandler(MySecurityException.class)
    public ResponseEntity<ErrorBody> error(MySecurityException e){
        log.warn("发生了异常",e);
        return new ResponseEntity<>(
                ErrorBody.builder().body("token非法，不允许访问").status(HttpStatus.UNAUTHORIZED.value()).build(),HttpStatus.UNAUTHORIZED
        );
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class ErrorBody{
    private String body;
    private int status;
}