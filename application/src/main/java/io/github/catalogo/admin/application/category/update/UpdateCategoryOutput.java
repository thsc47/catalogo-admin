package io.github.catalogo.admin.application.category.update;

import io.github.catalogo.admin.domain.category.Category;
import io.github.catalogo.admin.domain.category.CategoryId;

public record UpdateCategoryOutput(CategoryId id) {

    public static UpdateCategoryOutput from(Category aCategory) {
        return new UpdateCategoryOutput(aCategory.getId());
    }
}
