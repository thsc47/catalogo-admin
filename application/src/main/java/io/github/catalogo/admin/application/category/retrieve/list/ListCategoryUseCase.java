package io.github.catalogo.admin.application.category.retrieve.list;

import io.github.catalogo.admin.application.UseCase;
import io.github.catalogo.admin.domain.category.CategorySearchQuery;
import io.github.catalogo.admin.domain.pagination.Pagination;

public abstract class ListCategoryUseCase extends
        UseCase<CategorySearchQuery, Pagination<CategoryListOutput>> {
}
