package io.github.catalogo.admin.infrastructure.category;

import io.github.catalogo.admin.domain.category.Category;
import io.github.catalogo.admin.infrastructure.MySQLGatewayTest;
import io.github.catalogo.admin.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

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
}
