package io.github.catalogo.admin.infrastructure.category.presenters;

import io.github.catalogo.admin.application.category.retrieve.get.CategoryOutput;
import io.github.catalogo.admin.application.category.retrieve.list.CategoryListOutput;
import io.github.catalogo.admin.infrastructure.category.models.CategoryListResponse;
import io.github.catalogo.admin.infrastructure.category.models.CategoryResponse;

import java.util.function.Function;

public interface CategoryApiPresenter {

    Function<CategoryOutput, CategoryResponse> present =
            output -> new CategoryResponse(
                    output.id().getValue(),
                    output.name(),
                    output.description(),
                    output.isActive(),
                    output.createdAt(),
                    output.updatedAt(),
                    output.deletedAt());


    static CategoryListResponse present(final CategoryListOutput output) {
            return new CategoryListResponse(
                    output.id().getValue(),
                    output.name(),
                    output.description(),
                    output.isActive(),
                    output.createdAt(),
                    output.deletedAt()
            );
        }
}
