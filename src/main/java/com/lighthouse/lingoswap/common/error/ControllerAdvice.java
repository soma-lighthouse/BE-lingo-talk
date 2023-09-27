package com.lighthouse.lingoswap.common.error;

import com.lighthouse.lingoswap.common.service.ErrorResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static com.lighthouse.lingoswap.common.error.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Order
@RestControllerAdvice
public class ControllerAdvice {

    private final ErrorResponseService errorResponseService;

    @ExceptionHandler(Exception.class)
    private ResponseEntity handleUnexpectedException(final Exception ex) {
        log.error("{}", ex.getMessage());
        return errorResponseService.build(SERVER_ERROR);
    }

    @ExceptionHandler({
            HttpRequestMethodNotSupportedException.class,
            HttpMessageNotReadableException.class,
    })
    private ResponseEntity handleForbiddenException(final Exception ex) {
        log.error("{}", ex.getMessage());
        return errorResponseService.build(FORBIDDEN_ERROR);
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class
    })
    private ResponseEntity handleMethodArgumentNotValidException(final Exception ex) {
        log.error("{}", ex.getMessage());
        return errorResponseService.build(VALIDATION_ERROR);
    }
}