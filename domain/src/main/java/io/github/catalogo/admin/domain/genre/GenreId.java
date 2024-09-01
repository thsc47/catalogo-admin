package io.github.catalogo.admin.domain.genre;

import io.github.catalogo.admin.domain.Identifier;
import io.github.catalogo.admin.domain.category.CategoryId;

import java.util.Objects;
import java.util.UUID;

public class GenreId extends Identifier {

    private final String id;

    private GenreId(final String id) {
        Objects.requireNonNull(id, "Id cannot be null");
        this.id = id;
    }

    public static GenreId unique() {
        return GenreId.from(UUID.randomUUID());
    }

    public static GenreId from(final String anId) {
        return new GenreId(anId);
    }

    public static GenreId from(final UUID anId) {
        return new GenreId(anId.toString().toLowerCase());
    }

    @Override
    public String getValue() {
        return this.id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final GenreId that = (GenreId) o;
        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
