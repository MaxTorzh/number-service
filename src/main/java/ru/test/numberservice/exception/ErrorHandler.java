package ru.test.numberservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(final ValidationException e) {
        log.error("400 Bad Request: {}", e.getMessage(), e);
        String stackTrace = getStackTrace(e);
        return new ApiError(
                HttpStatus.BAD_REQUEST,
                "Validation Error",
                e.getMessage(),
                stackTrace
        );
    }

    @ExceptionHandler(FileProcessingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleFileProcessingException(final FileProcessingException e) {
        log.error("500 File processing error: {}", e.getMessage(), e);
        String stackTrace = getStackTrace(e);
        return new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "File processing error",
                e.getMessage(),
                stackTrace
        );
    }

    private String getStackTrace(Exception e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
