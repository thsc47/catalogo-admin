package io.github.catalogo.admin.infrastructure.api;

import io.github.catalogo.admin.infrastructure.category.models.CreateCategoryApiInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping("categories")
@Tag(name = "Categoris")
public interface CategoryAPI {

    @PostMapping(consumes = APPLICATION_JSON_VALUE,
                 produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was throw"),
            @ApiResponse(responseCode = "500", description = "An unexpected server error was throw")
    })
    ResponseEntity<?> createCategory(@RequestBody @Valid CreateCategoryApiInput input);

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "List all categories pagineted")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listed successfully"),
            @ApiResponse(responseCode = "422", description = "An invalid parameter was received"),
            @ApiResponse(responseCode = "500", description = "An unexpected server error was throw")
    })
    ResponseEntity<?> listCategories(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "nema") final String sort,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") final String direction

    );


}
