package io.github.catalogo.admin.application.category.delete;

import io.github.catalogo.admin.IntegrationTest;
import io.github.catalogo.admin.domain.category.Category;
import io.github.catalogo.admin.domain.category.CategoryGateway;
import io.github.catalogo.admin.domain.category.CategoryId;
import io.github.catalogo.admin.infrastructure.category.persistence.CategoryJpaEntity;
import io.github.catalogo.admin.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@IntegrationTest
public class DeleteCategoryUseCaseTestsIT {

    @Autowired
    private DeleteCategoryUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    @SpyBean
    private CategoryGateway gateway;

    @Test
    void givenAValidCommandWIthAValidId_whenCallsDeleteCategory_shouldBeOk() {
        final var expectedName = "Filmes";
        final var expectDescription = "A categoria mais assistida";
        final var expectIsActive = true;
        final var aCategory = Category.newCategory(expectedName, expectDescription, expectIsActive);
        final var expectedId = aCategory.getId();

        save(aCategory);
        assertEquals(1, repository.count());
        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        assertEquals(0, repository.count());
    }

    @Test
    void givenAValidCommandWIthAnInvalidId_whenCallsDeleteCategory_shouldReturnOk() {
        final var nonExistentId = CategoryId.from("123");

        assertEquals(0, repository.count());
        assertDoesNotThrow(() -> useCase.execute(nonExistentId.getValue()));
        assertEquals(0, repository.count());
    }

    @Test
    void givenAValidCommandWIthAValidId_whenGatewayThrowsAnRandomError_shouldReturnAnException() {
        final var nonExistentId = CategoryId.from("123");

        doThrow(new IllegalStateException("Gateway error")).when(gateway).deleteById(eq(nonExistentId));

        assertThrows(IllegalStateException.class, () -> useCase.execute(nonExistentId.getValue()));
    }

    private void save(Category... category) {
        repository.saveAllAndFlush(Arrays.stream(category)
                .map(CategoryJpaEntity::from)
                .toList());
    }
}
