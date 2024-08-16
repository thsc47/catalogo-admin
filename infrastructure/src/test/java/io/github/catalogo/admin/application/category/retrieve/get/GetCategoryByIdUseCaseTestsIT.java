package io.github.catalogo.admin.application.category.retrieve.get;

import io.github.catalogo.admin.IntegrationTest;
import io.github.catalogo.admin.domain.category.Category;
import io.github.catalogo.admin.domain.category.CategoryGateway;
import io.github.catalogo.admin.domain.category.CategoryId;
import io.github.catalogo.admin.domain.exceptions.DomainException;
import io.github.catalogo.admin.infrastructure.category.persistence.CategoryJpaEntity;
import io.github.catalogo.admin.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@IntegrationTest
public class GetCategoryByIdUseCaseTestsIT {

    @Autowired
    private GetCategoryByIddUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    @SpyBean
    private CategoryGateway gateway;

    @Test
    void givenAValidId_whenCallsGetById_shouldACategory() {
        final var expectedName = "Filmes";
        final var expectDescription = "A categoria mais assistida";
        final var expectIsActive = true;
        final var aCategory = Category.newCategory(expectedName, expectDescription, expectIsActive);
        final var expectedId = aCategory.getId();

        assertEquals(0, repository.count());
        repository.saveAndFlush(CategoryJpaEntity.from(aCategory));
        assertEquals(1, repository.count());

        useCase.execute(expectedId.getValue());

        final var actualCategory = repository.findById(expectedId.getValue()).get();

        assertNotNull(actualCategory);
        assertNotNull(actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectDescription, actualCategory.getDescription());
        assertEquals(expectIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    void givenAnInvalidId_whenCallsGetByID_shouldReturnNotFound() {
        final var expectedId = CategoryId.from("123");
        final var expectErrorMessage = format("Category with id %s was not found", expectedId.getValue());

        final var expectedException = assertThrows(DomainException.class,
                () -> useCase.execute(expectedId.getValue()));

        assertNotNull(expectedException);
        assertEquals(expectErrorMessage, expectedException.getErrors().get(0).message());
    }

    @Test
    void givenAValidId_whenGatewayThrowsAnRandomError_shouldReturnAnException() {
        final var nonExistentId = CategoryId.from("123");
        final var expectErrorMessage = "Gateway Error";

        doThrow(new IllegalStateException(expectErrorMessage))
            .when(gateway).findById(nonExistentId);

        final var expectedException = assertThrows(IllegalStateException.class, () ->
                useCase.execute(nonExistentId.getValue()));

        assertNotNull(expectedException);
        assertEquals(expectErrorMessage, expectedException.getMessage());
    }
}
