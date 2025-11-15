package ru.test.numberservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.test.numberservice.exception.FileProcessingException;
import ru.test.numberservice.validator.FileValidator;

import java.lang.reflect.Method;
import java.util.PriorityQueue;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NumberServiceTest {

    @Mock
    private FileValidator fileValidator;

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
    void findNthMinNumber_FileProcessingError_ThrowsException() {
        String filePath = "test.xlsx";
        int n = 3;

        doNothing().when(fileValidator).validateInput(filePath, n);

        assertThrows(FileProcessingException.class,
                () -> numberService.findNthMinNumber(filePath, n));
    }

    @Test
    void testIsInteger() throws Exception {
        Method isIntegerMethod = NumberService.class.getDeclaredMethod("isInteger", double.class);
        isIntegerMethod.setAccessible(true);

        assertTrue((Boolean) isIntegerMethod.invoke(numberService, 5.0));
        assertTrue((Boolean) isIntegerMethod.invoke(numberService, 0.0));
        assertFalse((Boolean) isIntegerMethod.invoke(numberService, 5.5));
        assertFalse((Boolean) isIntegerMethod.invoke(numberService, Double.POSITIVE_INFINITY));
    }
}
