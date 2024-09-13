package io.github.catalogo.admin.application.category.create;

import io.github.catalogo.admin.application.UseCaseTest;
import io.github.catalogo.admin.domain.category.CategoryGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTests extends UseCaseTest {

    @InjectMocks
    private DefaultCreateCategoryUseCase useCase;

    @Mock
    private CategoryGateway gateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
        final var expectedName = "Filmes";
        final var expectDescription = "A categoria mais assistida";
        final var expectIsActive = true;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectDescription, expectIsActive);

        when(gateway.create(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

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
        final var expectErrorCount = 1;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectDescription, expectIsActive);

        final var notification = useCase.execute(aCommand).getLeft();

        assertNotNull(notification);
        assertEquals(expectErrorCount, notification.getErrors().size());
        assertEquals(expectErrorMessage, notification.firtsError().message());

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

        final var actualOutput = useCase.execute(aCommand).get();

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
        final var expectErrorCount = 1;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectDescription, expectIsActive);

        when(gateway.create(Mockito.any()))
                .thenThrow(new IllegalStateException("gateway error test"));

        final var notification = useCase.execute(aCommand).getLeft();

        assertNotNull(notification);
        assertEquals(expectErrorCount, notification.getErrors().size());
        assertEquals(expectErrorMessage, notification.firtsError().message());

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
