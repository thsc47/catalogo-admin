package io.github.catalogo.admin.infrastructure.api.controllers;

import io.github.catalogo.admin.infrastructure.api.CategoryAPI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController implements CategoryAPI {

    @Override
    public ResponseEntity<?> createCategory() {
        return null;
    }

    @Override
    public ResponseEntity<?> listCategories(String search, int page, int perPage, String sort, String direction) {
        return null;
    }
}
