package ru.test.numberservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.test.numberservice.exception.ValidationException;

import java.util.PriorityQueue;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NumberServiceTest {

    @InjectMocks
    private NumberService numberService;

    @Test
    void testProcessNumberAlgorithm() {
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(3, Collections.reverseOrder());
        int[] numbers = {10, 5, 8, 3, 1, 9, 2, 7, 4, 6};

        for (int number : numbers) {
            if (maxHeap.size() < 3) {
                maxHeap.offer(number);
            } else if (number < maxHeap.peek()) {
                maxHeap.poll();
                maxHeap.offer(number);
            }
        }

        assertEquals(3, maxHeap.peek());
        assertEquals(3, maxHeap.size());

        assertTrue(maxHeap.contains(1));
        assertTrue(maxHeap.contains(2));
        assertTrue(maxHeap.contains(3));
    }

    @Test
    void testValidateInput_NegativeN() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> numberService.findNthMinNumber("test.xlsx", -1));
        assertEquals("Number N should be positive", exception.getMessage());
    }

    @Test
    void testValidateInput_EmptyFilePath() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> numberService.findNthMinNumber("", 5));
        assertEquals("Path to file cannot be empty", exception.getMessage());
    }

    @Test
    void testValidateInput_WrongFileFormat() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> numberService.findNthMinNumber("test.csv", 5));
        assertEquals("File should be xlsx extension", exception.getMessage());
    }
}
