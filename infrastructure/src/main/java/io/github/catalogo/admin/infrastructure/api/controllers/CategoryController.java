package io.github.catalogo.admin.infrastructure.api.controllers;

import io.github.catalogo.admin.application.category.create.CreateCategoryCommand;
import io.github.catalogo.admin.application.category.create.CreateCategoryUseCase;
import io.github.catalogo.admin.application.category.delete.DeleteCategoryUseCase;
import io.github.catalogo.admin.application.category.retrieve.get.GetCategoryByIddUseCase;
import io.github.catalogo.admin.application.category.retrieve.list.ListCategoryUseCase;
import io.github.catalogo.admin.application.category.update.UpdateCategoryCommand;
import io.github.catalogo.admin.application.category.update.UpdateCategoryUseCase;
import io.github.catalogo.admin.domain.pagination.SearchQuery;
import io.github.catalogo.admin.domain.pagination.Pagination;
import io.github.catalogo.admin.infrastructure.api.CategoryAPI;
import io.github.catalogo.admin.infrastructure.category.models.CategoryListResponse;
import io.github.catalogo.admin.infrastructure.category.models.CategoryResponse;
import io.github.catalogo.admin.infrastructure.category.models.CreateCategoryRequest;
import io.github.catalogo.admin.infrastructure.category.models.UpdateCategoryRequest;
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
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final ListCategoryUseCase listCategoryUseCase;

    public CategoryController(final CreateCategoryUseCase aCreateCategoryUseCase,
                              final GetCategoryByIddUseCase aGetCategoryByIddUseCase,
                              final UpdateCategoryUseCase aUpdateCategoryUseCase,
                              final DeleteCategoryUseCase aDeleteCategoryUseCase,
                              final ListCategoryUseCase aListCategoryUseCase) {
        this.createCategoryUseCase = requireNonNull(aCreateCategoryUseCase);
        this.getCategoryByIddUseCase = requireNonNull(aGetCategoryByIddUseCase);
        this.updateCategoryUseCase = requireNonNull(aUpdateCategoryUseCase);
        this.deleteCategoryUseCase = requireNonNull(aDeleteCategoryUseCase);
        this.listCategoryUseCase = requireNonNull(aListCategoryUseCase);
    }

    @Override
    public ResponseEntity<?> createCategory(final CreateCategoryRequest input) {
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
    public Pagination<CategoryListResponse> listCategories(String search, int page, int perPage, String sort, String direction) {
        final var aQuery = new SearchQuery(page, perPage, search, sort, direction);
        return this.listCategoryUseCase.execute(aQuery)
                .map(CategoryApiPresenter::present);
    }

    @Override
    public CategoryResponse getById(final String id) {

        return CategoryApiPresenter
                .present
                .compose(getCategoryByIddUseCase::execute)
                .apply(id);
    }

    @Override
    public ResponseEntity<?> updateById(String id, UpdateCategoryRequest input) {
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

    @Override
    public void deleteById(String id) {
        this.deleteCategoryUseCase.execute(id);
    }
}
