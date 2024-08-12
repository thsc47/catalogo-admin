package io.github.catalogo.admin.application.category.retrieve.list;

import io.github.catalogo.admin.domain.category.CategoryGateway;
import io.github.catalogo.admin.domain.category.CategorySearchQuery;
import io.github.catalogo.admin.domain.pagination.Pagination;

import java.util.Objects;

public class DefaultListCategoryUseCase extends ListCategoryUseCase {

    private final CategoryGateway gateway;

    public DefaultListCategoryUseCase(CategoryGateway aGateway) {
        this.gateway = Objects.requireNonNull(aGateway);
    }

    @Override
    public Pagination<CategoryListOutput> execute(final CategorySearchQuery aQuery) {
       return this.gateway.findAll(aQuery)
                .map(CategoryListOutput::from);
    }
}
