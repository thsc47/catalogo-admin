package io.github.catalogo.admin.infrastructure.category;

import io.github.catalogo.admin.infrastructure.MySQLGatewayTest;
import io.github.catalogo.admin.infrastructure.category.persistence.CategoryJpaEntity;
import io.github.catalogo.admin.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.github.catalogo.admin.domain.category.Category.newCategory;
import static org.junit.jupiter.api.Assertions.*;

@MySQLGatewayTest
public class CategoryMySQLTests {

    @Autowired
    private CategoryMySQLGateway mysqlGateway;

    @Autowired
    private CategoryRepository repository;

    @Test
    void givenAnValidCategory_whenCallsCreate_shouldReturnANewCategory() {
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
    void givenAnValidCategory_whenCallsUpdate_shouldReturnAUpdatedCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = newCategory("Filme", " ", false);

        assertEquals(0, repository.count());

        repository.saveAndFlush(CategoryJpaEntity.from(aCategory));
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
}
