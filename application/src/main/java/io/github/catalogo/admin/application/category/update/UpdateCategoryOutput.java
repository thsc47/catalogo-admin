package io.github.catalogo.admin.application.category.update;

import io.github.catalogo.admin.domain.category.Category;
import io.github.catalogo.admin.domain.category.CategoryId;

public record UpdateCategoryOutput(String id) {

    public static UpdateCategoryOutput from(final String aCategoryId) {
        return new UpdateCategoryOutput(aCategoryId);
    }

    public static UpdateCategoryOutput from(final Category aCategory) {
        return new UpdateCategoryOutput(aCategory.getId().getValue());
    }
}
