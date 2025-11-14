package ru.test.numberservice.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.test.numberservice.service.NumberService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NumberController.class)
@ExtendWith(MockitoExtension.class)
class NumberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NumberService numberService;

    @Test
    void findNthMinNumber_ValidRequest_ReturnsNumber() throws Exception {
        String filePath = "C:/test/numbers.xlsx";
        int n = 3;
        int expectedResult = 5;

        when(numberService.findNthMinNumber(filePath, n))
                .thenReturn(expectedResult);

        mockMvc.perform(post("/api/find-nth-min")
                        .param("filePath", filePath)
                        .param("n", String.valueOf(n))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(String.valueOf(expectedResult)));
    }

    @Test
    void findNthMinNumber_MissingFilePath_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/find-nth-min")
                        .param("n", "3")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findNthMinNumber_MissingN_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/find-nth-min")
                        .param("filePath", "C:/test/numbers.xlsx")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findNthMinNumber_NoParameters_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/find-nth-min")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findNthMinNumber_NonExcelFile_ReturnsBadRequest() throws Exception {
        String filePath = "C:/test/numbers.csv";
        int n = 3;

        when(numberService.findNthMinNumber(filePath, n))
                .thenThrow(new ru.test.numberservice.exception.ValidationException("File should be xlsx extension"));

        mockMvc.perform(post("/api/find-nth-min")
                        .param("filePath", filePath)
                        .param("n", String.valueOf(n))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findNthMinNumber_FileNotExists_ReturnsBadRequest() throws Exception {
        String filePath = "C:/test/nonexistent.xlsx";
        int n = 3;

        when(numberService.findNthMinNumber(filePath, n))
                .thenThrow(new ru.test.numberservice.exception.ValidationException("File does not exists"));

        mockMvc.perform(post("/api/find-nth-min")
                        .param("filePath", filePath)
                        .param("n", String.valueOf(n))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findNthMinNumber_NotEnoughNumbers_ReturnsBadRequest() throws Exception {
        String filePath = "C:/test/numbers.xlsx";
        int n = 10;

        when(numberService.findNthMinNumber(filePath, n))
                .thenThrow(new ru.test.numberservice.exception.ValidationException("Not enough numbers in file"));

        mockMvc.perform(post("/api/find-nth-min")
                        .param("filePath", filePath)
                        .param("n", String.valueOf(n))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findNthMinNumber_ServiceThrowsFileProcessingException_ReturnsInternalServerError() throws Exception {
        String filePath = "C:/test/numbers.xlsx";
        int n = 3;

        when(numberService.findNthMinNumber(filePath, n))
                .thenThrow(new ru.test.numberservice.exception.FileProcessingException("File read error"));

        mockMvc.perform(post("/api/find-nth-min")
                        .param("filePath", filePath)
                        .param("n", String.valueOf(n))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findNthMinNumber_LargeN_ReturnsCorrectResult() throws Exception {
        String filePath = "C:/test/large_numbers.xlsx";
        int n = 100;
        int expectedResult = 95;

        when(numberService.findNthMinNumber(filePath, n))
                .thenReturn(expectedResult);

        mockMvc.perform(post("/api/find-nth-min")
                        .param("filePath", filePath)
                        .param("n", String.valueOf(n))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(expectedResult)));
    }
}