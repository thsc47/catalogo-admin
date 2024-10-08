package io.github.catalogo.admin.domain.validation.handler;

import io.github.catalogo.admin.domain.exceptions.DomainException;
import io.github.catalogo.admin.domain.validation.Error;
import io.github.catalogo.admin.domain.validation.ValidationHandler;

import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {


    @Override
    public ValidationHandler append(Error anError) {
        throw DomainException.with(anError);
    }

    @Override
    public ValidationHandler append(ValidationHandler aHandler) {
        throw DomainException.with(aHandler.getErrors());
    }

    @Override
    public <T> T validate(Validation<T> aValidation) {
        try {
            return aValidation.validate();
        } catch(final Exception anException) {
            throw DomainException.with(List.of(new Error(anException.getMessage())));
        }
    }

    @Override
    public List<Error> getErrors() {
        return List.of();
    }
}
