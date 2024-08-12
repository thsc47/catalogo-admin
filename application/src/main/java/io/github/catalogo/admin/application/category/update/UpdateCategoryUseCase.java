package io.github.catalogo.admin.application.category.update;

import io.github.catalogo.admin.application.UseCase;
import io.github.catalogo.admin.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase
        extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {
}
