package io.github.catalogo.admin.application.category.update;

import io.github.catalogo.admin.domain.category.Category;
import io.github.catalogo.admin.domain.category.CategoryGateway;
import io.github.catalogo.admin.domain.category.CategoryId;
import io.github.catalogo.admin.domain.exceptions.DomainException;
import io.github.catalogo.admin.domain.exceptions.NotFoundException;
import io.github.catalogo.admin.domain.validation.Error;
import io.github.catalogo.admin.domain.validation.handler.Notification;
import io.vavr.control.Either;

import java.util.Objects;
import java.util.function.Supplier;

import static io.vavr.API.Left;
import static io.vavr.API.Try;
import static java.lang.String.format;

public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase {

    private final CategoryGateway gateway;

    public DefaultUpdateCategoryUseCase(final CategoryGateway aGateway) {
        this.gateway = Objects.requireNonNull(aGateway);
    }

    @Override
    public Either<Notification, UpdateCategoryOutput> execute(final UpdateCategoryCommand aCommand) {
        final var notification = Notification.create();
        final var anId = CategoryId.from(aCommand.id());
        final var aName = aCommand.name();
        final var aDescription = aCommand.description();
        final var isActive = aCommand.isActive();

        final var aCategory = this.gateway.findById(anId)
                .orElseThrow(notFound(anId));

        aCategory.update(aName, aDescription, isActive)
                .validate(notification);

        return notification.hasError() ? Left(notification) : update(aCategory);
    }

    private Either<Notification, UpdateCategoryOutput> update(final Category aCategory) {
        return Try(() -> this.gateway.update(aCategory))
                .toEither()
                .bimap(Notification::create, UpdateCategoryOutput::from);
    }

    private Supplier<NotFoundException> notFound(final CategoryId anId) {
        return () -> NotFoundException.with(Category.class, anId);
    }
}
