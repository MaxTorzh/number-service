package ru.test.numberservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.test.numberservice.service.NumberService;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Number Service", description = "Service for finding N-th min number in Excel file")
public class NumberController {

    private final NumberService numberService;

    @Operation(
            summary = "To find N-th min number in Excel file",
            description = "The method accepts an Excel file path and a number N and returns" +
                    " the N-th min number. Uses an algorithm with O(M log N) time complexity"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success search",
                    content = @Content(schema = @Schema(implementation = Integer.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Wrong request parameters or not enough numbers in the file"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error processing file or reading data"
            )
    })
    @PostMapping("/find-nth-min")
    public ResponseEntity<Integer> findNthMinNumber(
            @Parameter(
                    description = "Absolute path to the local Excel file",
                    required = true,
                    example = "C:/data/numbers.xlsx"
            )
            @RequestParam String filePath,

            @Parameter(
                    description = "The ordinal number of the minimum number (starting with 1)",
                    required = true,
                    example = "3"
            )
            @RequestParam int n) {

        log.info("Received request to find {}-th min number in file: {}", n, filePath);

        long startTime = System.currentTimeMillis();
        int result = numberService.findNthMinNumber(filePath, n);
        long endTime = System.currentTimeMillis();

        log.info("Found {}-th min number: {} (processing time: {} ms)", n, result, endTime - startTime);
        return ResponseEntity.ok(result);
    }
}
