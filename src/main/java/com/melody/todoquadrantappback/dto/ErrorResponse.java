package com.melody.todoquadrantappback.dto;

import java.time.Instant;
import com.melody.todoquadrantappback.exception.ErrorCode;

public class ErrorResponse {
    private final String code;
    private final String error;
    private final String message;
    private final String traceId;
    private final Instant timestamp;

    private ErrorResponse(String code, String error, String message, String traceId, Instant timestamp) {
        this.code = code;     // E401, E4031...
        this.error = error;   // Unauthorized, Forbidden
        this.message = message;
        this.traceId = traceId;
        this.timestamp = timestamp;
    }

    public static ErrorResponse from(ErrorCode errorCode, String traceId) {
        return new ErrorResponse(
                errorCode.getCode(),
                errorCode.getError(),
                errorCode.getMessage(),
                traceId,
                Instant.now()
        );
    }

    public String getCode() { return code; }
    public String getError() { return error; }
    public String getMessage() { return message; }
    public String getTraceId() { return traceId; }
    public Instant getTimestamp() { return timestamp; }
}
