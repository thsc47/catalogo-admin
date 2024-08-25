package io.github.catalogo.admin.infrastructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateCategoryRequest(
        String name,
        String description,
        @JsonProperty("is_active") Boolean active
) {
}
