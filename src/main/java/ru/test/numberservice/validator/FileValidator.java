package ru.test.numberservice.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.test.numberservice.exception.ValidationException;

import java.io.File;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileValidator {

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;
    private static final String[] ALLOWED_EXTENSIONS = {".xlsx"};

    /**
     * Валидация входных параметров и файла
     */
    public void validateInput(String filePath, int n) {
        validateParameters(filePath, n);
        validateFile(filePath);
        log.debug("Input validation passed - file: {}, n: {}", filePath, n);
    }

    /**
     * Валидация параметров
     */
    private void validateParameters(String filePath, int n) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new ValidationException("Path to file cannot be empty");
        }

        if (n <= 0) {
            throw new ValidationException("Number N should be positive");
        }
    }

    /**
     * Валидация файла
     */
    private void validateFile(String filePath) {
        validateFileExtension(filePath);

        File file = new File(filePath);
        validateFileExistence(file, filePath);
        validateFilePermissions(file, filePath);
        validateFileSize(file, filePath);
    }

    /**
     * Валидация расширения файла
     */
    private void validateFileExtension(String filePath) {
        String extension = getFileExtension(filePath).toLowerCase();

        boolean isValidExtension = false;
        for (String allowedExtension : ALLOWED_EXTENSIONS) {
            if (allowedExtension.equals(extension)) {
                isValidExtension = true;
                break;
            }
        }

        if (!isValidExtension) {
            throw new ValidationException(
                    String.format("Invalid file format. Expected %s, got: %s",
                            String.join(", ", ALLOWED_EXTENSIONS), extension)
            );
        }
    }

    /**
     * Валидация существования файла
     */
    private void validateFileExistence(File file, String filePath) {
        if (!file.exists()) {
            throw new ValidationException("File does not exist: " + filePath);
        }
    }

    /**
     * Валидация прав доступа
     */
    private void validateFilePermissions(File file, String filePath) {
        if (!file.canRead()) {
            throw new ValidationException("No read permissions for file: " + filePath);
        }
    }

    /**
     * Валидация размера файла
     */
    private void validateFileSize(File file, String filePath) {
        if (file.length() > MAX_FILE_SIZE) {
            throw new ValidationException(
                    String.format("File too large. Maximum size: %d MB, actual: %d MB",
                            MAX_FILE_SIZE / (1024 * 1024), file.length() / (1024 * 1024))
            );
        }

        if (file.length() == 0) {
            throw new ValidationException("File is empty: " + filePath);
        }
    }

    /**
     * Получение расширения файла
     */
    private String getFileExtension(String filePath) {
        int lastDotIndex = filePath.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filePath.length() - 1) {
            return "";
        }
        return filePath.substring(lastDotIndex);
    }
}
