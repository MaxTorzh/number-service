package ru.test.numberservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import ru.test.numberservice.exception.FileProcessingException;
import ru.test.numberservice.exception.InvalidFormatException;
import ru.test.numberservice.exception.ValidationException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.PriorityQueue;

@Slf4j
@Service
@RequiredArgsConstructor
public class NumberService {

    /**
     * Поиск N-го минимального числа в Excel файле
     * Используется  алгоритм с Max Heap
     *
     * @param filePath путь к Excel файлу
     * @param n порядковый номер минимального числа
     * @return N-ное минимальное число
     */
    public int findNthMinNumber(String filePath, int n) {
        log.debug("Starting search for {}-th min number in file: {}", n, filePath);
        validateInput(filePath, n);

        try (FileInputStream file = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheetAt(0);
            int result = findNthMinFromSheet(sheet, n);
            log.debug("Successfully found {}-th min number: {}", n, result);
            return result;

        } catch (IOException e) {
            throw new FileProcessingException("File reading error: " + filePath, e);
        } catch (InvalidFormatException e) {
            throw new ValidationException("Wrong file format. Expected .xlsx format");
        } catch (Exception e) {
            throw new FileProcessingException("Excel file processing error", e);
        }
    }

    /**
     * Алгоритм поиска N-ного минимального числа с использованием Max Heap
     * Временная сложность: O(M log N), где M - количество чисел, N - параметр
     * Память: O(N)
     */
    private int findNthMinFromSheet(Sheet sheet, int n) {
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(n, Collections.reverseOrder());

        int numbersProcessed = 0;

        for (Row row : sheet) {
            for (Cell cell : row) {
                if (cell.getCellType() == CellType.NUMERIC) {
                    int number = (int) Math.round(cell.getNumericCellValue());
                    processNumber(number, maxHeap, n);
                    numbersProcessed++;
                    log.trace("Processed number: {}, heap size: {}, heap: {}", number, maxHeap.size(), maxHeap);
                }
            }
        }

        if (numbersProcessed < n) {
            throw new ValidationException(
                    String.format("The file has %d numbers, but asked for %d-th min", numbersProcessed, n)
            );
        }
        int result = maxHeap.peek();
        log.debug("Found {}-th min number: {}", n, result);
        return result;
    }

    /**
     * Обработка очередного числа, обновляя Max Heap
     */
    private void processNumber(int number, PriorityQueue<Integer> maxHeap, int n) {
        if (maxHeap.size() < n) {
            maxHeap.offer(number);
        } else if (number < maxHeap.peek()) {
            maxHeap.poll();
            maxHeap.offer(number);
        }
    }

    /**
     * Валидация входных параметров
     */
    private void validateInput(String filePath, int n) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new ValidationException("Path to file cannot be empty");
        }
        if (!filePath.toLowerCase().endsWith(".xlsx")) {
            throw new ValidationException("File should be xlsx extension");
        }
        if (n <= 0) {
            throw new ValidationException("Number N should be positive");
        }

        java.io.File file = new java.io.File(filePath);
        if (!file.exists()) {
            throw new ValidationException("File does not exists: " + filePath);
        }
        if (!file.canRead()) {
            throw new ValidationException("No rights for reading: " + filePath);
        }
        log.debug("Input validation passed - file: {}, n: {}", filePath, n);
    }
}
