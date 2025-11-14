package ru.test.numberservice.util;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
public class TestFileCreator {

    public static void main(String[] args) {
        createTestFile();
    }

    public static void createTestFile() {
        String filePath = "C:/temp/test_numbers.xlsx";

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Numbers");

            int[] numbers = {12, 17, 41, 31, 54, 15, 11, 10, 66, 45, 32, 36};

            for (int i = 0; i < numbers.length; i++) {
                Row row = sheet.createRow(i);
                Cell cell = row.createCell(0);
                cell.setCellValue(numbers[i]);
            }

            sheet.autoSizeColumn(0);

            java.io.File tempDir = new java.io.File("C:/temp");
            if (!tempDir.exists()) {
                boolean created = tempDir.mkdirs();
                log.info("Directory created: {}", created);
            }

            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
                log.info("The file successful created: {}", filePath);

                java.io.File file = new java.io.File(filePath);
                log.info("Size: {} byte", file.length());
                log.info("Exists: {}", file.exists());
                log.info("Readable: {}", file.canRead());
            }

        } catch (IOException e) {
            log.error("Error file creation", e);
            e.printStackTrace();
        }
    }
}
