package io.github.catalogo.admin.infrastructure.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping("categories")
public interface CategoryAPI {

    @PostMapping(consumes = APPLICATION_JSON_VALUE,
                 produces = APPLICATION_JSON_VALUE)
    ResponseEntity<?> createCategory();

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    ResponseEntity<?> listCategories(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "nema") final String sort,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") final String direction

    );


}
