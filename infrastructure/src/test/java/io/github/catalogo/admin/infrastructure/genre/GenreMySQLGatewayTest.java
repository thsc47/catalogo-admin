package io.github.catalogo.admin.infrastructure.genre;

import io.github.catalogo.admin.MySQLGatewayTest;
import io.github.catalogo.admin.domain.category.Category;
import io.github.catalogo.admin.domain.category.CategoryId;
import io.github.catalogo.admin.domain.genre.Genre;
import io.github.catalogo.admin.infrastructure.category.CategoryMySQLGateway;
import io.github.catalogo.admin.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.wildfly.common.Assert.assertNotNull;

@MySQLGatewayTest
public class GenreMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryGateway;

    @Autowired
    private GenreMySQLGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void testDependenciesInjected() {
        assertNotNull(categoryGateway);
        assertNotNull(genreGateway);
        assertNotNull(genreRepository);
    }

    @Test
    public void givenAValidGenre_whenCallsCreateGenre_shouldPersistGenre() {
        final var filmes =
                categoryGateway.create(Category.newCategory("Filmes", null, true));

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes.getId());

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
        aGenre.addCategories(expectedCategories);

        final var expectedId = aGenre.getId();

        assertEquals(0, genreRepository.count());

        final var actualGenre = genreGateway.create(aGenre);

        assertEquals(1, genreRepository.count());

        assertEquals(expectedId, actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
        assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
        assertNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, persistedGenre.getName());
        assertEquals(expectedIsActive, persistedGenre.isActive());
        assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
        assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        assertEquals(aGenre.getUpdatedAt(), persistedGenre.getUpdatedAt());
        assertEquals(aGenre.getDeletedAt(), persistedGenre.getDeletedAt());
        assertNull(persistedGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenreWithoutCategories_whenCallsCreateGenre_shouldPersistGenre() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryId>of();

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);

        final var expectedId = aGenre.getId();

        assertEquals(0, genreRepository.count());

        final var actualGenre = genreGateway.create(aGenre);

        assertEquals(1, genreRepository.count());

        assertEquals(expectedId, actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
        assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
        assertNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, persistedGenre.getName());
        assertEquals(expectedIsActive, persistedGenre.isActive());
        assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
        assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        assertEquals(aGenre.getUpdatedAt(), persistedGenre.getUpdatedAt());
        assertEquals(aGenre.getDeletedAt(), persistedGenre.getDeletedAt());
        assertNull(persistedGenre.getDeletedAt());
    }
}
