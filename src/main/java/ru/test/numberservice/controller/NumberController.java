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
import org.springframework.web.bind.annotation.*;
import ru.test.numberservice.exception.ApiError;
import ru.test.numberservice.service.NumberService;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Number Service", description = "Service for finding Nth minimum number in Excel files")
public class NumberController {

    private final NumberService numberService;

    @Operation(
            summary = "Find Nth minimum number",
            description = "Finds the Nth minimum number in an Excel file"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful search",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Wrong request parameters or not enough numbers in the file",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error processing file or reading data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            )
    })
    @PostMapping("/find-nth-min")
    public ResponseEntity<Integer> findNthMinNumber(
            @RequestParam
            @Parameter(description = "Path to Excel file", example = "/path/to/file.xlsx")
            String filePath,

            @RequestParam
            @Parameter(description = "N-th minimum number to find", example = "5")
            int n) {

        log.info("Received request to find {}-th min number in file: {}", n, filePath);
        int result = numberService.findNthMinNumber(filePath, n);
        return ResponseEntity.ok(result);
    }
}