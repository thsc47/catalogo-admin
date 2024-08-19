package io.github.catalogo.admin.infrastructure.api.controllers;

import io.github.catalogo.admin.application.category.create.CreateCategoryUseCase;
import io.github.catalogo.admin.infrastructure.api.CategoryAPI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;

    public CategoryController(final CreateCategoryUseCase aCreateCategoryUseCase) {
        this.createCategoryUseCase = requireNonNull(aCreateCategoryUseCase);
    }

    @Override
    public ResponseEntity<?> createCategory() {
        return null;
    }

    @Override
    public ResponseEntity<?> listCategories(String search, int page, int perPage, String sort, String direction) {
        return null;
    }
}
