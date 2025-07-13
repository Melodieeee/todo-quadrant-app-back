package com.melody.todoquadrantappback.exception;
import org.springframework.http.HttpStatus;

public enum ErrorCode {

    UNAUTHORIZED("E401", HttpStatus.UNAUTHORIZED, "Unauthorized", "User not authenticated."),
    FORBIDDEN("E403", HttpStatus.FORBIDDEN, "Forbidden", "Access denied."),
    USER_NOT_FOUND("E4031", HttpStatus.FORBIDDEN, "Forbidden", "User not found."),
    TASK_FORBIDDEN("E4032", HttpStatus.FORBIDDEN, "Forbidden", "You are not allowed to access this resource."),
    TASK_NOT_FOUND("E404", HttpStatus.NOT_FOUND, "Not Found", "Task not found."),
    METHOD_NOT_ALLOWED("E405", HttpStatus.METHOD_NOT_ALLOWED, "Method Not Allowed", "Request method is not supported."),
    VALIDATION_ERROR("E400", HttpStatus.BAD_REQUEST, "Bad Request", "Validation failed."),
    INTERNAL_ERROR("E500", HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "Internal server error.");

    private final String code; // send to frontend to handle different error codes
    private final HttpStatus httpStatus;
    private final String error;
    private final String message;

    ErrorCode(String code, HttpStatus httpStatus, String error, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.error = error;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}

