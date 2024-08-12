package io.github.catalogo.admin.application.category.delete;

import io.github.catalogo.admin.domain.category.CategoryGateway;
import io.github.catalogo.admin.domain.category.CategoryId;

import java.util.Objects;

public class DefaultDeleteCategoryUseCase extends DeleteCategoryUseCase {

    private final CategoryGateway gateway;

    public DefaultDeleteCategoryUseCase(final CategoryGateway aGateway) {
        this.gateway = Objects.requireNonNull(aGateway);
    }

    @Override
    public void execute(String anId) {
        this.gateway.deleteById(CategoryId.from(anId));
    }
}
