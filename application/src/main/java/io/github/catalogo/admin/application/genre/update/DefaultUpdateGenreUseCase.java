package io.github.catalogo.admin.application.genre.update;

import io.github.catalogo.admin.domain.Identifier;
import io.github.catalogo.admin.domain.category.CategoryGateway;
import io.github.catalogo.admin.domain.category.CategoryId;
import io.github.catalogo.admin.domain.exceptions.DomainException;
import io.github.catalogo.admin.domain.exceptions.NotFoundException;
import io.github.catalogo.admin.domain.exceptions.NotificationException;
import io.github.catalogo.admin.domain.genre.Genre;
import io.github.catalogo.admin.domain.genre.GenreGateway;
import io.github.catalogo.admin.domain.genre.GenreId;
import io.github.catalogo.admin.domain.validation.Error;
import io.github.catalogo.admin.domain.validation.ValidationHandler;
import io.github.catalogo.admin.domain.validation.handler.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DefaultUpdateGenreUseCase extends UpdateGenreUseCase {

    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;

    public DefaultUpdateGenreUseCase(
            final CategoryGateway categoryGateway,
            final GenreGateway genreGateway
    ) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public UpdateGenreOutput execute(final UpdateGenreCommand aCommand) {
        final var anId = GenreId.from(aCommand.id());
        final var aName = aCommand.name();
        final var isActive = aCommand.isActive();
        final var categories = toCategoryId(aCommand.categories());

        final var aGenre = this.genreGateway.findById(anId)
                .orElseThrow(notFound(anId));

        final var notification = Notification.create();
        notification.append(validateCategories(categories));
        notification.validate(() -> aGenre.update(aName, isActive, categories));

        if (notification.hasError()) {
            throw new NotificationException(
                    "Could not update Aggregate Genre %s".formatted(aCommand.id()), notification
            );
        }

        return UpdateGenreOutput.from(this.genreGateway.update(aGenre));
    }

    private ValidationHandler validateCategories(List<CategoryId> ids) {
        final var notification = Notification.create();
        if (ids == null || ids.isEmpty()) {
            return notification;
        }

        final var retrievedIds = categoryGateway.existsByIds(ids);

        if (ids.size() != retrievedIds.size()) {
            final var missingIds = new ArrayList<>(ids);
            missingIds.removeAll(retrievedIds);

            final var missingIdsMessage = missingIds.stream()
                    .map(CategoryId::getValue)
                    .collect(Collectors.joining(", "));

            notification.append(new Error("Some categories could not be found: %s".formatted(missingIdsMessage)));
        }

        return notification;
    }

    private Supplier<DomainException> notFound(final Identifier anId) {
        return () -> NotFoundException.with(Genre.class, anId);
    }

    private List<CategoryId> toCategoryId(final List<String> categories) {
        return categories.stream()
                .map(CategoryId::from)
                .toList();
    }
}
