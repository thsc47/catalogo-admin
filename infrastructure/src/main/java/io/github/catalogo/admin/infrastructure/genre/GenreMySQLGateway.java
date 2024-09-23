package io.github.catalogo.admin.infrastructure.genre;

import io.github.catalogo.admin.domain.genre.Genre;
import io.github.catalogo.admin.domain.genre.GenreGateway;
import io.github.catalogo.admin.domain.genre.GenreId;
import io.github.catalogo.admin.domain.pagination.Pagination;
import io.github.catalogo.admin.domain.pagination.SearchQuery;
import io.github.catalogo.admin.infrastructure.genre.persistence.GenreJpaEntity;
import io.github.catalogo.admin.infrastructure.genre.persistence.GenreRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class GenreMySQLGateway implements GenreGateway {

    private final GenreRepository genreRepository;

    public GenreMySQLGateway(final GenreRepository genreRepository) {
        this.genreRepository = Objects.requireNonNull(genreRepository);
    }

    @Override
    public Genre create(final Genre aGenre) {
        return save(aGenre);
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
        return save(aGenre);
    }

    @Override
    public Pagination<Genre> findAll(SearchQuery aQuery) {
        return null;
    }

    private Genre save(final Genre aGenre) {
        return this.genreRepository.save(GenreJpaEntity.from(aGenre))
                .toAggregate();
    }
}
