package io.github.catalogo.admin.infrastructure.category.presenters;

import io.github.catalogo.admin.application.category.retrieve.get.CategoryOutput;
import io.github.catalogo.admin.infrastructure.category.models.CategoryOutputApi;

import java.util.function.Function;

public interface CategoryApiPresenter {

    Function<CategoryOutput, CategoryOutputApi> present =
            output -> new CategoryOutputApi(
                    output.id().getValue(),
                    output.name(),
                    output.description(),
                    output.isActive(),
                    output.createdAt(),
                    output.updatedAt(),
                    output.deletedAt());
}
