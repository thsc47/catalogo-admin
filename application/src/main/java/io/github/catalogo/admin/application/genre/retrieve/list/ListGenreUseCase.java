package io.github.catalogo.admin.application.genre.retrieve.list;

import io.github.catalogo.admin.application.UseCase;
import io.github.catalogo.admin.domain.pagination.Pagination;
import io.github.catalogo.admin.domain.pagination.SearchQuery;

public abstract class ListGenreUseCase
        extends UseCase<SearchQuery, Pagination<GenreListOutput>> {
}
