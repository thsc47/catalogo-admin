package io.github.catalogo.admin.application.category.create;

import io.github.catalogo.admin.IntegrationTest;
import io.github.catalogo.admin.domain.category.CategoryGateway;
import io.github.catalogo.admin.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@IntegrationTest
public class CreateCategoryUseCaseTestsIT {

    @Autowired
    private CreateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    @SpyBean
    private CategoryGateway gateway;

    @Test
    void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
        final var expectedName = "Filmes";
        final var expectDescription = "A categoria mais assistida";
        final var expectIsActive = true;

        assertEquals(0, repository.count());
        final var aCommand = CreateCategoryCommand.with(expectedName, expectDescription, expectIsActive);

        final var actualOutput = useCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var actualCategory = repository.findById(actualOutput.id().getValue()).get();
        assertEquals(1, repository.count());

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectDescription, actualCategory.getDescription());
        assertEquals(expectIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    void givenAnInvalidName_whenCallsCreateCategory_shouldThrowAnDomainException() {
        final String expectedName = null;
        final var expectDescription = "A categoria mais assistida";
        final var expectIsActive = true;
        final var expectErrorMessage = "'name' should not be null";
        final var expectErrorCount = 1;

        assertEquals(0, repository.count());
        final var aCommand = CreateCategoryCommand.with(expectedName, expectDescription, expectIsActive);

        final var notification = useCase.execute(aCommand).getLeft();

        assertNotNull(notification);
        assertEquals(expectErrorCount, notification.getErrors().size());
        assertEquals(expectErrorMessage, notification.firtsError().message());
        assertEquals(0, repository.count());
    }

    @Test
    void givenAValidCommandWithInactiveCategory_whenCallsCreate_shouldReturnInactiveCategoryId() {
        final var expectedName = "Filmes";
        final var expectDescription = "A categoria mais assistida";
        final var expectIsActive = false;

        assertEquals(0, repository.count());
        final var aCommand = CreateCategoryCommand.with(expectedName, expectDescription, expectIsActive);

        final var actualOutput = useCase.execute(aCommand).get();

        final var actualCategory = repository.findById(actualOutput.id().getValue()).get();
        assertEquals(1, repository.count());

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectDescription, actualCategory.getDescription());
        assertEquals(expectIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAnException() {
        final var expectedName = "Filmes";
        final var expectDescription = "A categoria mais assistida";
        final var expectIsActive = true;
        final var expectErrorMessage = "gateway error test";
        final var expectErrorCount = 1;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectDescription, expectIsActive);

        doThrow(new IllegalStateException("gateway error test")).
                when(gateway).create(Mockito.any());


        final var notification = useCase.execute(aCommand).getLeft();

        assertNotNull(notification);
        assertEquals(expectErrorCount, notification.getErrors().size());
        assertEquals(expectErrorMessage, notification.firtsError().message());
    }
}
