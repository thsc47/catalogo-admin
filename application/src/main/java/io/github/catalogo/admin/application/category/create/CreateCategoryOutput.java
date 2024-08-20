package io.github.catalogo.admin.application.category.create;

import io.github.catalogo.admin.domain.category.Category;
import io.github.catalogo.admin.domain.category.CategoryId;

public record CreateCategoryOutput(CategoryId id) {
    public static CreateCategoryOutput from(final Category aCategory) {
        return new CreateCategoryOutput(aCategory.getId());
    }

    public static CreateCategoryOutput from(final CategoryId aCategoryId) {
        return new CreateCategoryOutput(aCategoryId);
    }
}
