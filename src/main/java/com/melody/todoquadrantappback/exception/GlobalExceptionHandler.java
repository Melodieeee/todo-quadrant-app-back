package com.melody.todoquadrantappback.exception;

import com.melody.todoquadrantappback.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.UUID;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private String generateTraceId(String scope) {
        return scope + "-" + UUID.randomUUID();
    }

    private ResponseEntity<ErrorResponse> buildResponse(ErrorCode code) {
        String traceId = generateTraceId(code.name().toLowerCase());
        log.warn("[{}] Error occurred: {}", traceId, code.name());
        return ResponseEntity
                .status(code.getHttpStatus())
                .body(ErrorResponse.from(code, traceId));
    }


    @ExceptionHandler(UserUnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUserUnauthorized(UserUnauthorizedException ex) {
        return buildResponse(ErrorCode.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        return buildResponse(ErrorCode.USER_NOT_FOUND);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTaskNotFound(TaskNotFoundException ex) {
        return buildResponse(ErrorCode.TASK_NOT_FOUND);
    }

    @ExceptionHandler(TaskForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleTaskForbidden(TaskForbiddenException ex) {
        return buildResponse(ErrorCode.TASK_FORBIDDEN);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        return buildResponse(ErrorCode.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String detail = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        String traceId = generateTraceId("validation");
        log.warn("[{}] Validation failed: {}", traceId, detail);

        ErrorResponse response = ErrorResponse.from(ErrorCode.VALIDATION_ERROR, traceId);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        String traceId = generateTraceId("internal");
        log.error("[{}] Unexpected error: {}", traceId, ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.from(ErrorCode.INTERNAL_ERROR, traceId));
    }
}
