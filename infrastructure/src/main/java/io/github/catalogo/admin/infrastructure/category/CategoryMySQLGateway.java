package io.github.catalogo.admin.infrastructure.category;

import io.github.catalogo.admin.domain.category.Category;
import io.github.catalogo.admin.domain.category.CategoryGateway;
import io.github.catalogo.admin.domain.category.CategoryId;
import io.github.catalogo.admin.domain.category.CategorySearchQuery;
import io.github.catalogo.admin.domain.pagination.Pagination;
import io.github.catalogo.admin.infrastructure.category.persistence.CategoryJpaEntity;
import io.github.catalogo.admin.infrastructure.category.persistence.CategoryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.print.attribute.standard.PageRanges;
import java.util.Optional;

import static io.github.catalogo.admin.infrastructure.category.persistence.CategoryJpaEntity.from;
import static io.github.catalogo.admin.infrastructure.utils.SpecificationUtils.like;

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

    public Pagination<Category> findAll(final CategorySearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var specifications = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(str -> {
                    final Specification<CategoryJpaEntity> nameLike = like("name", str);
                    final Specification<CategoryJpaEntity> descriptionLike = like("description", str);
                    return nameLike.or(descriptionLike);
                })
                .orElse(null);

        final var pageResult =
                this.categoryRepository.findAll(Specification.where(specifications), page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CategoryJpaEntity::toAggregate).toList()
        );
    }

    @Override
    public Optional<Category> findById(final CategoryId anId) {
        return this.categoryRepository.findById(anId.getValue())
                .map(CategoryJpaEntity::toAggregate);
    }

    @Override
    public void deleteById(final CategoryId anId) {
        final var anIdValue = anId.getValue();
        if (this.categoryRepository.existsById(anIdValue)) {
            this.categoryRepository.deleteById(anIdValue);
        }
    }
}
