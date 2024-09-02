package io.github.catalogo.admin.domain.genre;

import io.github.catalogo.admin.domain.validation.Error;
import io.github.catalogo.admin.domain.validation.ValidationHandler;
import io.github.catalogo.admin.domain.validation.Validator;

import static java.lang.String.format;

public class GenreValidator extends Validator {

    final static String FIELD_CANNOT_BE_NULL_ERROR_MESSAGE = "'%s' should not be null";
    final static String FIELD_CANNOT_BE_BLANK_ERROR_MESSAGE = "'%s' should not be blank";
    final static String FIELD_WITH_INVALID_LENGTH_ERROR_MESSAGE = "'%s' should be between 1 and 255 characters";

    private final Genre genre;

    protected GenreValidator(final Genre aGenre,
                             final ValidationHandler aHandler) {
        super(aHandler);
        this.genre = aGenre;
    }

    @Override
    protected void validate() {
        final var name = this.genre.getName();
        if (name == null) {
            this.validationHandler().append(new Error(format(FIELD_CANNOT_BE_NULL_ERROR_MESSAGE, "name")));
            return;
        }

        if (name.isBlank()) {
            this.validationHandler().append(new Error(format(FIELD_CANNOT_BE_BLANK_ERROR_MESSAGE, "name")));
            return;
        }

        final int nameLength = name.trim().length();
        if (nameLength < 1 || nameLength > 255) {
            this.validationHandler().append(new Error(format(FIELD_WITH_INVALID_LENGTH_ERROR_MESSAGE, "name")));
        }
    }
}
