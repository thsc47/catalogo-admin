package io.github.catalogo.admin.application.genre.retrieve.get;

import io.github.catalogo.admin.domain.exceptions.NotFoundException;
import io.github.catalogo.admin.domain.genre.Genre;
import io.github.catalogo.admin.domain.genre.GenreGateway;
import io.github.catalogo.admin.domain.genre.GenreId;

import java.util.Objects;

public class DefaultGetGenreByIdUseCase extends GetGenreByIdUseCase{

    private final GenreGateway genreGateway;

    public DefaultGetGenreByIdUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public GenreOutput execute(final String anIn) {
        final var aGenreId = GenreId.from(anIn);
        return this.genreGateway.findById(aGenreId)
                .map(GenreOutput::from)
                .orElseThrow(() -> NotFoundException.with(Genre.class, aGenreId));
    }
}
