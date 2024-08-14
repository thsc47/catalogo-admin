package io.github.catalogo.admin.infrastructure.category;

import io.github.catalogo.admin.domain.category.Category;
import io.github.catalogo.admin.domain.category.CategoryGateway;
import io.github.catalogo.admin.domain.category.CategoryId;
import io.github.catalogo.admin.domain.category.CategorySearchQuery;
import io.github.catalogo.admin.domain.pagination.Pagination;
import io.github.catalogo.admin.infrastructure.category.persistence.CategoryRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static io.github.catalogo.admin.infrastructure.category.persistence.CategoryJpaEntity.from;

@Component
public class CategoryMySQLGateway implements CategoryGateway {

    private final CategoryRepository categoryRepository;

    public CategoryMySQLGateway(final CategoryRepository aCategoryRepository) {
        this.categoryRepository = aCategoryRepository;
    }

    @Override
    public Category create(final Category aCategory) {
        return save(aCategory);
    }

    @Override
    public Category update(final Category aCategory) {
        return save(aCategory);
    }

    private Category save(final Category aCategory) {
        return this.categoryRepository.save(from(aCategory)).toAggregate();
    }

    @Override
    public Pagination<Category> findAll(final CategorySearchQuery aQuery) {
        return null;
    }

    @Override
    public Optional<Category> findById(final CategoryId anId) {
        return Optional.empty();
    }

    @Override
    public void deleteById(final CategoryId anId) {
    }
}
