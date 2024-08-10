package io.github.catalogo.admin.domain.category;

import io.github.catalogo.admin.domain.validation.Error;
import io.github.catalogo.admin.domain.validation.ValidationHandler;
import io.github.catalogo.admin.domain.validation.Validator;

public class CategoryValidator extends Validator {
    final static String NAME_CANNOT_BE_NULL_ERROR_MESSAGE = "Name should not be null";
    private final Category category;

    public CategoryValidator(final Category aCategory, final ValidationHandler aHandler) {
        super(aHandler);
        this.category = aCategory;
    }

    @Override
    protected void validate() {
        if(this.category.getName() == null) {
            this.validationHandler().append(new Error(NAME_CANNOT_BE_NULL_ERROR_MESSAGE));
        }
    }
}
