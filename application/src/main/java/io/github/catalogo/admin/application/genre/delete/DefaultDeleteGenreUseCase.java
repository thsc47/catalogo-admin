package io.github.catalogo.admin.application.genre.delete;

import io.github.catalogo.admin.domain.genre.GenreGateway;
import io.github.catalogo.admin.domain.genre.GenreId;

import java.util.Objects;

public class DefaultDeleteGenreUseCase extends DeleteGenreUseCase {

    private final GenreGateway genreGateway;

    public DefaultDeleteGenreUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public void execute(final String anIn) {
        this.genreGateway.deleteById(GenreId.from(anIn));
    }
}
