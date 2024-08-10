package io.github.catalogo.admin.domain.validation;

public abstract class Validator {

    private final ValidationHandler handler;

    protected Validator(final ValidationHandler aHandler) {
        this.handler = aHandler;
    }

    protected ValidationHandler validationHandler() {
        return this.handler;
    }

    protected abstract void validate();

}
