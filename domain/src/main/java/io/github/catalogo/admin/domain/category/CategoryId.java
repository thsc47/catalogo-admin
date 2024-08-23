package io.github.catalogo.admin.domain.category;

import io.github.catalogo.admin.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class CategoryId extends Identifier {
    private final String id;

    private CategoryId(final String id) {
        Objects.requireNonNull(id, "Id cannot be null");
        this.id = id;
    }

    public static CategoryId unique() {
        return CategoryId.from(UUID.randomUUID());
    }

    public static CategoryId from(final String anId) {
        return new CategoryId(anId);
    }

    public static CategoryId from(final UUID anId) {
        return new CategoryId(anId.toString().toLowerCase());
    }

    @Override
    public String getValue() {
        return this.id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CategoryId that = (CategoryId) o;
        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
