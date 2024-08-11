package io.github.catalogo.admin.application.category.create;

import io.github.catalogo.admin.domain.category.Category;
import io.github.catalogo.admin.domain.category.CategoryGateway;
import io.github.catalogo.admin.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTests {

    @InjectMocks
    private DefaultCreateCategoryUseCase useCase;

    @Mock
    private CategoryGateway gateway;

    @Test
    void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
        final var expectedName = "Filmes";
        final var expectDescription = "A categoria mais assistida";
        final var expectIsActive = true;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectDescription, expectIsActive);

        when(gateway.create(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(gateway).create(argThat(aCategory ->
                Objects.equals(expectedName, aCategory.getName())
                        && Objects.equals(expectDescription, aCategory.getDescription())
                        && Objects.equals(expectIsActive, aCategory.isActive())
                        && Objects.nonNull(aCategory.getId())
                        && Objects.nonNull(aCategory.getUpdatedAt())
                        && Objects.nonNull(aCategory.getCreatedAt())
                        && Objects.isNull(aCategory.getDeletedAt())));
    }

    @Test
    void givenAnInvalidName_whenCallsCreateCategory_shouldThrowAnDomainException() {
        final String expectedName = null;
        final var expectDescription = "A categoria mais assistida";
        final var expectIsActive = true;
        final var expectErrorMessage = "'name' should not be null";

        final var aCommand = CreateCategoryCommand.with(expectedName, expectDescription, expectIsActive);

        final var expectException = assertThrows(DomainException.class, () ->
                useCase.execute(aCommand));

        assertNotNull(expectException);
        assertEquals(expectErrorMessage, expectException.getErrors().get(0).message());

        verify(gateway, never()).create(any());
    }

    @Test
    void givenAValidCommandWithInactiveCategory_whenCallsCreate_shouldReturnInactiveCategoryId() {
        final var expectedName = "Filmes";
        final var expectDescription = "A categoria mais assistida";
        final var expectIsActive = false;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectDescription, expectIsActive);

        when(gateway.create(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(gateway).create(argThat(aCategory ->
                Objects.equals(expectedName, aCategory.getName())
                        && Objects.equals(expectDescription, aCategory.getDescription())
                        && Objects.equals(expectIsActive, aCategory.isActive())
                        && Objects.nonNull(aCategory.getId())
                        && Objects.nonNull(aCategory.getUpdatedAt())
                        && Objects.nonNull(aCategory.getCreatedAt())
                        && Objects.nonNull(aCategory.getDeletedAt())));
    }

    @Test
    void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAnException() {
        final var expectedName = "Filmes";
        final var expectDescription = "A categoria mais assistida";
        final var expectIsActive = true;
        final var expectErrorMessage = "gateway error test";

        final var aCommand = CreateCategoryCommand.with(expectedName, expectDescription, expectIsActive);

        when(gateway.create(Mockito.any()))
                .thenThrow(new IllegalStateException("gateway error test"));

        final var actualException = assertThrows(IllegalStateException.class, () ->
                useCase.execute(aCommand));

        assertNotNull(actualException);
        assertEquals(expectErrorMessage, actualException.getMessage());

        verify(gateway).create(argThat(aCategory ->
                Objects.equals(expectedName, aCategory.getName())
                        && Objects.equals(expectDescription, aCategory.getDescription())
                        && Objects.equals(expectIsActive, aCategory.isActive())
                        && Objects.nonNull(aCategory.getId())
                        && Objects.nonNull(aCategory.getUpdatedAt())
                        && Objects.nonNull(aCategory.getCreatedAt())
                        && Objects.isNull(aCategory.getDeletedAt())));
    }
}
