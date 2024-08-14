package io.github.catalogo.admin.infrastructure.category;

import io.github.catalogo.admin.domain.category.Category;
import io.github.catalogo.admin.domain.category.CategoryId;
import io.github.catalogo.admin.domain.category.CategorySearchQuery;
import io.github.catalogo.admin.infrastructure.MySQLGatewayTest;
import io.github.catalogo.admin.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.github.catalogo.admin.domain.category.Category.newCategory;
import static io.github.catalogo.admin.infrastructure.category.persistence.CategoryJpaEntity.from;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.*;

@MySQLGatewayTest
public class CategoryMySQLTests {

    @Autowired
    private CategoryMySQLGateway mysqlGateway;

    @Autowired
    private CategoryRepository repository;
    @Autowired
    private CategoryMySQLGateway categoryMySQLGateway;

    @Test
    void givenAValidCategory_whenCallsCreate_shouldReturnANewCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = newCategory(expectedName, expectedDescription, expectedIsActive);

        assertEquals(0, repository.count());

        final var actualCategory = mysqlGateway.create(aCategory);

        assertEquals(1, repository.count());
        assertNotNull(actualCategory.getId());
        assertEquals(aCategory.getName(), actualCategory.getName());
        assertEquals(aCategory.getDescription(), actualCategory.getDescription());
        assertEquals(aCategory.isActive(), actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        assertNull(actualCategory.getDeletedAt());

        final var actualEntity = repository.findById(aCategory.getId().getValue()).get();

        assertNotNull(actualEntity.getId());
        assertEquals(aCategory.getName(), actualEntity.getName());
        assertEquals(aCategory.getDescription(), actualEntity.getDescription());
        assertEquals(aCategory.isActive(), actualEntity.isActive());
        assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
        assertEquals(aCategory.getUpdatedAt(), actualEntity.getUpdatedAt());
        assertEquals(aCategory.getDeletedAt(), actualEntity.getDeletedAt());
        assertNull(actualEntity.getDeletedAt());
    }

    @Test
    void givenAValidCategory_whenCallsUpdate_shouldReturnAUpdatedCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = newCategory("Filme", " ", false);

        assertEquals(0, repository.count());

        repository.saveAndFlush(from(aCategory));
        assertEquals(1, repository.count());

        final var anUpdatedCategory = aCategory.clone().update(expectedName, expectedDescription, expectedIsActive);
        final var actualCategory = mysqlGateway.update(anUpdatedCategory);

        assertEquals(1, repository.count());
        assertNotNull(actualCategory.getId());
        assertEquals(anUpdatedCategory.getName(), actualCategory.getName());
        assertEquals(anUpdatedCategory.getDescription(), actualCategory.getDescription());
        assertEquals(anUpdatedCategory.isActive(), actualCategory.isActive());
        assertEquals(anUpdatedCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(anUpdatedCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertEquals(anUpdatedCategory.getDeletedAt(), actualCategory.getDeletedAt());
        assertNull(actualCategory.getDeletedAt());

        final var actualEntity = repository.findById(actualCategory.getId().getValue()).get();

        assertNotNull(actualEntity.getId());
        assertEquals(anUpdatedCategory.getName(), actualEntity.getName());
        assertEquals(anUpdatedCategory.getDescription(), actualEntity.getDescription());
        assertEquals(anUpdatedCategory.isActive(), actualEntity.isActive());
        assertEquals(anUpdatedCategory.getCreatedAt(), actualEntity.getCreatedAt());
        assertEquals(anUpdatedCategory.getUpdatedAt(), actualEntity.getUpdatedAt());
        assertEquals(anUpdatedCategory.getDeletedAt(), actualEntity.getDeletedAt());
        assertNull(actualEntity.getDeletedAt());
    }

    @Test
    void givenAPrePersistedCategoryWithAValidCategoryId_whenTryToDeleted_shouldDeleteACategory() {
        final var aCategory = newCategory("Filmes", "", false);

        assertEquals(0, repository.count());
        repository.saveAndFlush(from(aCategory));
        assertEquals(1, repository.count());

        categoryMySQLGateway.deleteById(aCategory.getId());

        assertEquals(0, repository.count());
    }

    @Test
    void givenAPrePersistedCategoryWithAnInvalidCategoryId_whenTryToDeleted_shouldReturn() {
        assertEquals(0, repository.count());
        assertDoesNotThrow(() -> categoryMySQLGateway.deleteById(CategoryId.from("123")));
        assertEquals(0, repository.count());
    }

    @Test
    void givenAPrePersistedCategoryWithAValidCategoryId_whenCallsFindById_shouldReturnACategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = newCategory(expectedName, expectedDescription, expectedIsActive);

        assertEquals(0, repository.count());
        repository.saveAndFlush(from(aCategory));
        assertEquals(1, repository.count());

        final var actualCategory = mysqlGateway.findById(aCategory.getId()).get();

        assertEquals(1, repository.count());
        assertNotNull(actualCategory.getId());
        assertEquals(aCategory.getName(), actualCategory.getName());
        assertEquals(aCategory.getDescription(), actualCategory.getDescription());
        assertEquals(aCategory.isActive(), actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    void givenAPrePersistedCategoryWithAnInvalidCategoryId_whenCallsFindById_shouldReturnEmpty() {
        assertEquals(0, repository.count());

        final var actualCategory = mysqlGateway.findById(CategoryId.from("123"));

        assertTrue(actualCategory.isEmpty());
    }

    @Test
    void givenAPrePersistedCategory_whenCallsFindAll_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var categoryFilmes = Category.newCategory("Filmes", null, true);
        final var categorySeries = Category.newCategory("Series", null, true);
        final var categoryDocumentarios = Category.newCategory("Documentarios", null, true);

        assertEquals(0, repository.count());
        repository.saveAll(of(from(categoryDocumentarios), from(categoryFilmes), from(categorySeries)));
        assertEquals(3, repository.count());

        final var aQuery = new CategorySearchQuery(0, 1, "", "name", "asc");
        final var actualResult = categoryMySQLGateway.findAll(aQuery);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(categoryDocumentarios.getId(), actualResult.items().get(0).getId());
    }

    @Test
    void givenEmptyCategoryTable_whenCallsFindAll_shouldReturnEmptyPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;

        assertEquals(0, repository.count());

        final var aQuery = new CategorySearchQuery(0, 1, "", "name", "asc");
        final var actualResult = categoryMySQLGateway.findAll(aQuery);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedTotal, actualResult.items().size());
    }

    @Test
    void givenFollowPagination_whenCallsFindAllWithPageOne_shouldReturnPaginated() {
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var categoryFilmes = Category.newCategory("Filmes", null, true);
        final var categorySeries = Category.newCategory("Series", null, true);
        final var categoryDocumentarios = Category.newCategory("Documentarios", null, true);

        assertEquals(0, repository.count());
        repository.saveAll(of(from(categoryDocumentarios), from(categoryFilmes), from(categorySeries)));
        assertEquals(3, repository.count());

        //Page 0
        var aQuery = new CategorySearchQuery(0, 1, "", "name", "asc");
        var actualResult = categoryMySQLGateway.findAll(aQuery);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(categoryDocumentarios.getId(), actualResult.items().get(0).getId());

        //Page 1
        expectedPage = 1;
        aQuery = new CategorySearchQuery(1, 1, "", "name", "asc");
        actualResult = categoryMySQLGateway.findAll(aQuery);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(categoryFilmes.getId(), actualResult.items().get(0).getId());
    }

    @Test
    void givenAPrePersistedCategoryAndDocAsTerms_whenCallsFindAllAndTermsMatchs_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var categoryFilmes = Category.newCategory("Filmes", null, true);
        final var categorySeries = Category.newCategory("Series", null, true);
        final var categoryDocumentarios = Category.newCategory("Documentarios", null, true);

        assertEquals(0, repository.count());
        repository.saveAll(of(from(categoryDocumentarios), from(categoryFilmes), from(categorySeries)));
        assertEquals(3, repository.count());

        final var aQuery = new CategorySearchQuery(0, 1, "doc", "name", "asc");
        final var actualResult = categoryMySQLGateway.findAll(aQuery);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(categoryDocumentarios.getId(), actualResult.items().get(0).getId());
    }

    @Test
    void givenAPrePersistedCategoryAndMaisAssistidaAsTerms_whenCallsFindAllAndTermsMatchs_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var categoryFilmes = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var categorySeries = Category.newCategory("Series", "Uma categoria assistida", true);
        final var categoryDocumentarios = Category.newCategory("Documentarios", " A categoria menos assistida", true);

        assertEquals(0, repository.count());
        repository.saveAll(of(from(categoryDocumentarios), from(categoryFilmes), from(categorySeries)));
        assertEquals(3, repository.count());

        final var aQuery = new CategorySearchQuery(0, 1, "mais assistida", "name", "asc");
        final var actualResult = categoryMySQLGateway.findAll(aQuery);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(categoryFilmes.getId(), actualResult.items().get(0).getId());
    }
}
