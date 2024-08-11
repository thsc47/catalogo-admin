package io.github.catalogo.admin.application.category.create;

import io.github.catalogo.admin.application.UseCase;
import io.github.catalogo.admin.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase
        extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {

}
