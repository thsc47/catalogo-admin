package io.github.catalogo.admin.domain.category;

import io.github.catalogo.admin.domain.AggregatedRoot;
import io.github.catalogo.admin.domain.validation.ValidationHandler;

import java.time.Instant;

import static java.util.Objects.requireNonNull;

public class Category extends AggregatedRoot<CategoryId> implements Cloneable {

    private String name;
    private String description;
    private boolean active;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

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
        this.createdAt = requireNonNull(createdAt, "'createdAt' cannot be null");
        this.updatedAt = requireNonNull(updatedAt, "'updatedAt' cannot be null");
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

    public static Category with(final CategoryId anId,
                                final String name,
                                final String description,
                                final boolean active,
                                final Instant createdAt,
                                final Instant updatedAt,
                                final Instant deletedAt) {
        return new Category(anId, name, description, active, createdAt, updatedAt, deletedAt);
    }

    public CategoryId getId() {
        return id;
    }

    @Override
    public void validate(ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }

    public Category deactivate() {
        if (getDeletedAt() == null) {
            this.deletedAt = Instant.now();
        }
        this.active = false;
        this.updatedAt = Instant.now();
        return this;
    }


    public Category activate() {
        this.active = true;
        this.updatedAt = Instant.now();
        this.deletedAt = null;
        return this;
    }

    public Category update(final String aName, final String aDescription, final boolean isActive) {
        this.name = aName;
        this.description = aDescription;
        if (isActive) {
            activate();
        } else {
            deactivate();
        }
        this.updatedAt = Instant.now();
        return this;
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

    @Override
    public Category clone() {
        try {
            return (Category) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}