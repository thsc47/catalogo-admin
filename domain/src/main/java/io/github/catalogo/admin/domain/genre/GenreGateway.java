package io.github.catalogo.admin.domain.genre;

import io.github.catalogo.admin.domain.pagination.Pagination;
import io.github.catalogo.admin.domain.pagination.SearchQuery;

import java.util.Optional;

public interface GenreGateway {

    Genre create(Genre aGenre);

    void deleteById(GenreId anId);

    Optional<Genre> findById(GenreId anId);

    Genre update(Genre aGenre);

    Pagination<Genre> findAll(SearchQuery aQuery);
}
