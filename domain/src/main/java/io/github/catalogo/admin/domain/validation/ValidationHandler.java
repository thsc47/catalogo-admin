package io.github.catalogo.admin.domain.validation;

import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(final Error anError);

    ValidationHandler append(final ValidationHandler aHandler);

    <T> T validate(final Validation<T> aValidation);

    List<Error> getErrors();

    default boolean hasError() {
        return getErrors() != null && !getErrors().isEmpty();
    }

    default Error firtsError() {
        if (getErrors().isEmpty()) {
            return null;
        }
        return getErrors().get(0);
    }

    interface Validation<T> {
        T validate();
    }
}
