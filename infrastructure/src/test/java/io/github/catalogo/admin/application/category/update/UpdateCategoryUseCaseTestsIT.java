package io.github.catalogo.admin.application.category.update;

import io.github.catalogo.admin.IntegrationTest;
import io.github.catalogo.admin.domain.category.Category;
import io.github.catalogo.admin.domain.category.CategoryGateway;
import io.github.catalogo.admin.domain.exceptions.DomainException;
import io.github.catalogo.admin.infrastructure.category.persistence.CategoryJpaEntity;
import io.github.catalogo.admin.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@IntegrationTest
public class UpdateCategoryUseCaseTestsIT {

    @Autowired
    private UpdateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    @SpyBean
    private CategoryGateway gateway;

    @Test
    void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
        final var expectedName = "Filmes";
        final var expectDescription = "A categoria mais assistida";
        final var expectIsActive = true;
        final var aCategory = Category.newCategory("Filme", " ", true);
        final var expectedId = aCategory.getId();

        assertEquals(0, repository.count());
        save(aCategory);
        assertEquals(1, repository.count());
        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(),
                expectedName, expectDescription, expectIsActive);

        final var actualOutput = useCase.execute(aCommand).get();
        final var actualCategory = repository.findById(actualOutput.id()).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        assertNotNull(actualCategory);
        assertNotNull(actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectDescription, actualCategory.getDescription());
        assertEquals(expectIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
    }

    @Test
    void givenAnInvalidName_whenCallsUpdateCategory_shouldReturnAnDomainException() {
        final String invalidName = null;
        final var expectDescription = "A categoria mais assistida";
        final var expectIsActive = true;
        final var aCategory = Category.newCategory("Filme", " ", true);
        final var expectedId = aCategory.getId();
        final var expectErrorMessage = "'name' should not be null";
        final var expectErrorCount = 1;

        save(aCategory);

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                invalidName,
                expectDescription,
                expectIsActive);

        final var notification = useCase.execute(aCommand).getLeft();

        assertNotNull(notification);
        assertEquals(expectErrorCount, notification.getErrors().size());
        assertEquals(expectErrorMessage, notification.firtsError().message());

        verify(gateway).findById(eq(expectedId));
        verify(gateway, never()).update(any());
    }

    @Test
    void givenAValidInactivateCommand_whenCallsUpdate_shouldReturnInactiveCategoryId() {
        final var expectedName = "Filmes";
        final var expectDescription = "A categoria mais assistida";
        final var expectIsActive = false;

        final var aCategory = Category.newCategory("Filme", " ", true);
        save(aCategory);
        assertTrue(aCategory.isActive());
        assertNull(aCategory.getDeletedAt());

        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(),
                expectedName, expectDescription, expectIsActive);

        final var actualOutput = useCase.execute(aCommand).get();

        final var actualCategory = repository.findById(actualOutput.id()).get();

        assertNotNull(actualCategory);
        assertNotNull(actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectDescription, actualCategory.getDescription());
        assertEquals(expectIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
    }

    @Test
    void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAnException() {
        final var expectedName = "Filmes";
        final var expectDescription = "A categoria mais assistida";
        final var expectIsActive = true;
        final var aCategory = Category.newCategory("Filme", " ", true);
        final var expectedId = aCategory.getId();
        final var expectErrorMessage = "gateway error test";
        final var expectErrorCount = 1;

        save(aCategory);
        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectDescription, expectIsActive);

        doThrow(new IllegalStateException("gateway error test"))
            .when(gateway).update(any());

        final var notification = useCase.execute(aCommand).getLeft();

        assertNotNull(notification);
        assertEquals(expectErrorCount, notification.getErrors().size());
        assertEquals(expectErrorMessage, notification.firtsError().message());
    }

    @Test
    void givenACommandWithInvalidId_whenCallsUpdateCategory_shouldReturnNotFound() {
        final var expectedName = "Filmes";
        final var expectDescription = "A categoria mais assistida";
        final var expectIsActive = true;
        final var nonExistentId = "123";
        final var expectedErrorMessage = format("Category with id %s was not found", nonExistentId);
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCategoryCommand.with(nonExistentId,
                expectedName, expectDescription, expectIsActive);

        final var actualException = assertThrows(DomainException.class,
                () -> useCase.execute(aCommand).get());

        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    private void save(Category... aCategoryList) {
        repository.saveAllAndFlush(Arrays.stream(aCategoryList)
                .map(CategoryJpaEntity::from).toList());
    }
}
