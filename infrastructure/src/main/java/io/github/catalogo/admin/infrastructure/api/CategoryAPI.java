package io.github.catalogo.admin.infrastructure.api;

import io.github.catalogo.admin.domain.pagination.Pagination;
import io.github.catalogo.admin.infrastructure.category.models.CategoryOutputApi;
import io.github.catalogo.admin.infrastructure.category.models.CreateCategoryApiInput;
import io.github.catalogo.admin.infrastructure.category.models.UpdateCategoryApiInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.NO_CONTENT;
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
    Pagination<?> listCategories(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "nema") final String sort,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") final String direction

    );

    @GetMapping(value = "{id}",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Get a Category by identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listed successfully"),
            @ApiResponse(responseCode = "404", description = "A Category was not found"),
            @ApiResponse(responseCode = "500", description = "An unexpected server error was throw")
    })
    CategoryOutputApi getById(@PathVariable(name = "id") String id);

    @PutMapping(value = "{id}",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Get a Category by identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "404", description = "A Category was not found"),
            @ApiResponse(responseCode = "500", description = "An unexpected server error was throw")
    })
    ResponseEntity<?> updateById(@PathVariable(name = "id") String id,
                                 @RequestBody @Valid UpdateCategoryApiInput input);

    @DeleteMapping(value = "{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Delete a category by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    void deleteById(@PathVariable(name = "id") String id);
}
