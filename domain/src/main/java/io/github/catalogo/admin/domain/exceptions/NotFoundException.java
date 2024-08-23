package io.github.catalogo.admin.domain.exceptions;

import io.github.catalogo.admin.domain.AggregatedRoot;
import io.github.catalogo.admin.domain.Identifier;
import io.github.catalogo.admin.domain.validation.Error;

import java.util.Collections;
import java.util.List;

import static java.lang.String.format;

public class NotFoundException extends DomainException {

    protected NotFoundException(final String aMessage,
                                final List<Error> anErrors) {
        super(aMessage, anErrors);
    }

    public static NotFoundException with(final Class<? extends AggregatedRoot<?>> anAggregate,
                                         final Identifier anIdentifier) {
        final var anError =
                format("%s with id %s not found", anAggregate.getSimpleName(), anIdentifier.getValue());
        return new NotFoundException(anError, Collections.emptyList());
    }
}
