package ru.test.numberservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import ru.test.numberservice.exception.FileProcessingException;
import ru.test.numberservice.exception.ValidationException;
import ru.test.numberservice.validator.FileValidator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.PriorityQueue;

@Slf4j
@Service
@RequiredArgsConstructor
public class NumberService {

    private final FileValidator fileValidator;

    /**
     * Поиск N-го минимального числа в Excel файле
     * Используется алгоритм с Max Heap
     */
    public int findNthMinNumber(String filePath, int n) {
        log.debug("Starting search for {}-th min number in file: {}", n, filePath);

        fileValidator.validateInput(filePath, n);

        try (FileInputStream file = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(file)) {

            Sheet sheet = workbook.getSheetAt(0);
            int result = findNthMinFromSheet(sheet, n);
            log.debug("Successfully found {}-th min number: {}", n, result);
            return result;

        } catch (IOException e) {
            throw new FileProcessingException("File reading error: " + filePath, e);
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
            if (row == null) {
                log.trace("Skipping null row");
                continue;
            }

            for (Cell cell : row) {
                if (cell == null) {
                    log.trace("Skipping null cell");
                    continue;
                }

                if (cell.getCellType() == CellType.NUMERIC) {
                    double value = cell.getNumericCellValue();

                    if (isInteger(value)) {
                        int number = (int) value;
                        processNumber(number, maxHeap, n);
                        numbersProcessed++;
                        log.trace("Processed number: {}, heap size: {}, heap: {}", number, maxHeap.size(), maxHeap);
                    } else {
                        log.warn("Skipping non-integer number: {}", value);
                    }

                } else if (cell.getCellType() == CellType.STRING) {
                    try {
                        String cellValue = cell.getStringCellValue().trim();
                        if (!cellValue.isEmpty()) {
                            int number = Integer.parseInt(cellValue);
                            processNumber(number, maxHeap, n);
                            numbersProcessed++;
                            log.trace("Processed number from string: {}, heap size: {}", number, maxHeap.size());
                        }
                    } catch (NumberFormatException e) {
                        log.warn("Skipping non-numeric string: {}", cell.getStringCellValue());
                    }
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
     * Проверка на целое число
     */
    private boolean isInteger(double value) {
        return value == Math.floor(value) && !Double.isInfinite(value);
    }
}
