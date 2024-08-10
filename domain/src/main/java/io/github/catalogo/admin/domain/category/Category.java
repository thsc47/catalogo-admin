package io.github.catalogo.admin.domain.category;

import io.github.catalogo.admin.domain.AggregatedRoot;
import io.github.catalogo.admin.domain.validation.ValidationHandler;

import java.time.Instant;

public class Category extends AggregatedRoot<CategoryId> {

    private final String name;
    private final String description;
    private final boolean active;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final Instant deletedAt;

    private Category(final CategoryId anId,
                    final String name,
                    final String description,
                    final boolean active,
                    final Instant createdAt,
                    final Instant updatedAt,
                    final Instant deletedAt) {
        super(anId);
        this.name = name;
        this.description = description;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static Category newCategory(final String aName,
                                       final String aDescription,
                                       final boolean isActive) {
        final var id = CategoryId.unique();
        final var nowTimestamp = Instant.now();
        final var deletedAt = isActive ? null : nowTimestamp;
        return new Category(id, aName, aDescription, isActive, nowTimestamp, nowTimestamp, deletedAt);
    }

    public CategoryId getId() {
        return id;
    }

    @Override
    public void validate(ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }
}