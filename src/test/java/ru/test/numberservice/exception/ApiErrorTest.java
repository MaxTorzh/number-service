package ru.test.numberservice.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit тесты для ApiError класса.
 * Проверяют корректность создания объектов ошибок различными способами.
 */
class ApiErrorTest {

    @Test
    void apiError_AllArgsConstructor_CreatesObject() {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String reason = "Test reason";
        String message = "Test message";
        List<String> errors = List.of("Error 1", "Error 2");
        LocalDateTime timestamp = LocalDateTime.now();

        ApiError apiError = new ApiError(status, reason, message, errors, timestamp);

        assertEquals(status, apiError.getStatus());
        assertEquals(reason, apiError.getReason());
        assertEquals(message, apiError.getMessage());
        assertEquals(errors, apiError.getErrors());
        assertEquals(timestamp, apiError.getTimestamp());
    }

    @Test
    void apiError_ConstructorWithStackTrace_CreatesObject() {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String reason = "Test reason";
        String message = "Test message";
        String stackTrace = "Test stack trace";

        ApiError apiError = new ApiError(status, reason, message, stackTrace);

        assertEquals(status, apiError.getStatus());
        assertEquals(reason, apiError.getReason());
        assertEquals(message, apiError.getMessage());
        assertEquals(List.of(stackTrace), apiError.getErrors());
        assertNotNull(apiError.getTimestamp());
    }

    @Test
    void apiError_Builder_CreatesObject() {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .reason("Not Found")
                .message("Resource not found")
                .errors(List.of("Stack trace"))
                .timestamp(LocalDateTime.now())
                .build();

        assertNotNull(apiError);
        assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
        assertEquals("Not Found", apiError.getReason());
        assertEquals("Resource not found", apiError.getMessage());
        assertFalse(apiError.getErrors().isEmpty());
        assertNotNull(apiError.getTimestamp());
    }

    @Test
    void apiError_NoArgsConstructor_CreatesObject() {
        ApiError apiError = new ApiError();

        assertNotNull(apiError);
        assertNull(apiError.getStatus());
        assertNull(apiError.getReason());
        assertNull(apiError.getMessage());
        assertNull(apiError.getErrors());
        assertNull(apiError.getTimestamp());
    }
}