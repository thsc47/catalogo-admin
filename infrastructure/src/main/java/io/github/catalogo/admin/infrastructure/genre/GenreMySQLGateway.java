package io.github.catalogo.admin.infrastructure.genre;

import io.github.catalogo.admin.domain.genre.Genre;
import io.github.catalogo.admin.domain.genre.GenreGateway;
import io.github.catalogo.admin.domain.genre.GenreId;
import io.github.catalogo.admin.domain.pagination.Pagination;
import io.github.catalogo.admin.domain.pagination.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GenreMySQLGateway implements GenreGateway {

    @Override
    public Genre create(Genre aGenre) {
        return null;
    }

    @Override
    public void deleteById(GenreId anId) {

    }

    @Override
    public Optional<Genre> findById(GenreId anId) {
        return Optional.empty();
    }

    @Override
    public Genre update(Genre aGenre) {
        return null;
    }

    @Override
    public Pagination<Genre> findAll(SearchQuery aQuery) {
        return null;
    }
}
