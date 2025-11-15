package ru.test.numberservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.test.numberservice.service.NumberService;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Number Service")
public class NumberController {

    private final NumberService numberService;

    @Operation(summary = "Find Nth minimum number in Excel file")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "third_min", value = "11"),
                            @ExampleObject(name = "first_min", value = "5")
                    }
            )),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
                {
                  "status": "BAD_REQUEST",
                  "reason": "Validation Error",
                  "message": "File should be xlsx extension",
                  "errors": ["Validation failed"],
                  "timestamp": "2025-11-10:00:06:26"
                }
                """)
            )),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
                {
                  "status": "INTERNAL_SERVER_ERROR",
                  "reason": "File processing error",
                  "message": "Excel file processing error",
                  "errors": ["Internal error"],
                  "timestamp": "2025-11-10:00:06:27"
                }
                """)
            ))
    })
    @PostMapping("/find-nth-min")
    public int findNthMinNumber(
            @RequestParam @Parameter(example = "/data/numbers.xlsx") String filePath,
            @RequestParam @Parameter(example = "3") int n) {

        return numberService.findNthMinNumber(filePath, n);
    }
}