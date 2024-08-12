package io.github.catalogo.admin.application.category.retrieve.get;

import io.github.catalogo.admin.domain.category.Category;
import io.github.catalogo.admin.domain.category.CategoryGateway;
import io.github.catalogo.admin.domain.category.CategoryId;
import io.github.catalogo.admin.domain.exceptions.DomainException;
import io.github.catalogo.admin.domain.validation.Error;

import java.util.Objects;
import java.util.function.Supplier;

import static java.lang.String.format;

public class DefaultGetCategoryByIddUseCase extends GetCategoryByIddUseCase {

    private final CategoryGateway gateway;

    public DefaultGetCategoryByIddUseCase(final CategoryGateway aGateway) {
        this.gateway = Objects.requireNonNull(aGateway);
    }

    @Override
    public CategoryOutput execute(final String anId) {
        return this.gateway.findById(CategoryId.from(anId))
                .map(CategoryOutput::from)
                .orElseThrow(NotFound(anId));
    }

    private static Supplier<DomainException> NotFound(final String anId) {
        return () -> DomainException.with(new Error(format("Category with id %s was not found", anId)));
    }
}
