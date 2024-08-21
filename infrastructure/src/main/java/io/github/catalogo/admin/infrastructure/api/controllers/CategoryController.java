package io.github.catalogo.admin.infrastructure.api.controllers;

import io.github.catalogo.admin.application.category.create.CreateCategoryCommand;
import io.github.catalogo.admin.application.category.create.CreateCategoryUseCase;
import io.github.catalogo.admin.infrastructure.api.CategoryAPI;
import io.github.catalogo.admin.infrastructure.category.models.CreateCategoryApiInput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static java.util.Objects.requireNonNull;

@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;

    public CategoryController(final CreateCategoryUseCase aCreateCategoryUseCase) {
        this.createCategoryUseCase = requireNonNull(aCreateCategoryUseCase);
    }

    @Override
    public ResponseEntity<?> createCategory(final CreateCategoryApiInput input) {
        final var aCommand =
                CreateCategoryCommand.with(
                        input.name(),
                        input.description(),
                        input.active() != null ? input.active() : true);

        return this.createCategoryUseCase.execute(aCommand)
                .fold(
                        ResponseEntity.unprocessableEntity()::body,
                        output -> ResponseEntity.created(
                                URI.create("/categories/" + output.id().getValue())).build()
                );
    }

    @Override
    public ResponseEntity<?> listCategories(String search, int page, int perPage, String sort, String direction) {
        return null;
    }
}
