package io.github.catalogo.admin.application.category.retrieve.list;

import io.github.catalogo.admin.application.UseCase;
import io.github.catalogo.admin.domain.pagination.SearchQuery;
import io.github.catalogo.admin.domain.pagination.Pagination;

public abstract class ListCategoryUseCase extends
        UseCase<SearchQuery, Pagination<CategoryListOutput>> {
}
