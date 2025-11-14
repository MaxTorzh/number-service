package ru.test.numberservice.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "API Error response")
public class ApiError {

    @Schema(description = "HTTP status", example = "BAD_REQUEST")
    private HttpStatus status;

    @Schema(description = "Error reason", example = "Validation Error")
    private String reason;

    @Schema(description = "Error message", example = "File should be xlsx extension")
    private String message;

    @Schema(description = "List of errors")
    private List<String> errors;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd:HH:mm:ss")
    @Schema(description = "Timestamp of error", example = "2025-11-10:00:06:26")
    private LocalDateTime timestamp;

    public ApiError(HttpStatus status, String reason, String message, String stackTrace) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.errors = List.of(stackTrace);
        this.timestamp = LocalDateTime.now();
    }
}