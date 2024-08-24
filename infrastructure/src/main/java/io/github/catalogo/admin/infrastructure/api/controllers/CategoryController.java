package io.github.catalogo.admin.infrastructure.api.controllers;

import io.github.catalogo.admin.application.category.create.CreateCategoryCommand;
import io.github.catalogo.admin.application.category.create.CreateCategoryUseCase;
import io.github.catalogo.admin.application.category.retrieve.get.GetCategoryByIddUseCase;
import io.github.catalogo.admin.application.category.update.UpdateCategoryCommand;
import io.github.catalogo.admin.application.category.update.UpdateCategoryUseCase;
import io.github.catalogo.admin.infrastructure.api.CategoryAPI;
import io.github.catalogo.admin.infrastructure.category.models.CategoryOutputApi;
import io.github.catalogo.admin.infrastructure.category.models.CreateCategoryApiInput;
import io.github.catalogo.admin.infrastructure.category.models.UpdateCategoryApiInput;
import io.github.catalogo.admin.infrastructure.category.presenters.CategoryApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static java.util.Objects.requireNonNull;

@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryByIddUseCase getCategoryByIddUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;

    public CategoryController(final CreateCategoryUseCase aCreateCategoryUseCase,
                              final GetCategoryByIddUseCase aGetCategoryByIddUseCase,
                              final UpdateCategoryUseCase aUpdateCategoryUseCase) {
        this.createCategoryUseCase = requireNonNull(aCreateCategoryUseCase);
        this.getCategoryByIddUseCase = requireNonNull(aGetCategoryByIddUseCase);
        this.updateCategoryUseCase = requireNonNull(aUpdateCategoryUseCase);
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

    @Override
    public CategoryOutputApi getById(final String id) {

        return CategoryApiPresenter
                .present
                .compose(getCategoryByIddUseCase::execute)
                .apply(id);
    }

    @Override
    public ResponseEntity<?> updateById(String id, UpdateCategoryApiInput input) {
        final var aCommand =
                UpdateCategoryCommand.with(
                        id,
                        input.name(),
                        input.description(),
                        input.active() != null ? input.active() : true);

        return this.updateCategoryUseCase.execute(aCommand)
                .fold(
                        ResponseEntity.unprocessableEntity()::body,
                        output -> ResponseEntity.ok().body(output)
                );
    }
}
