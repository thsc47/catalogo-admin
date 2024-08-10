package io.github.catalogo.admin.domain.category;

import io.github.catalogo.admin.domain.pagination.Pagination;

import java.util.Optional;

public interface CategoryGateway {

    Category create(Category aCategory);

    Category update(Category aCategory);

    Pagination<Category> findAll(CategorySearchQuery aQuery);

    Optional<Category> findById(CategoryId anId);

    void deleteById(CategoryId anId);

}
