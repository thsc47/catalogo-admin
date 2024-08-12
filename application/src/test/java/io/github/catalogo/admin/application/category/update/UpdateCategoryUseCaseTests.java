package io.github.catalogo.admin.application.category.update;

import io.github.catalogo.admin.domain.category.Category;
import io.github.catalogo.admin.domain.category.CategoryGateway;
import io.github.catalogo.admin.domain.category.CategoryId;
import io.github.catalogo.admin.domain.exceptions.DomainException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Optional.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTests {

    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Mock
    private CategoryGateway gateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(gateway);
    }

    @Test
    void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
        final var expectedName = "Filmes";
        final var expectDescription = "A categoria mais assistida";
        final var expectIsActive = true;
        final var aCategory = Category.newCategory("Filme", " ", true);
        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(),
                expectedName, expectDescription, expectIsActive);

        when(gateway.findById(eq(expectedId))).thenReturn(of(aCategory));
        when(gateway.update(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(gateway).findById(eq(expectedId));
        verify(gateway).update(argThat(anUpdatedCategory ->
                Objects.equals(expectedName, anUpdatedCategory.getName())
                        && Objects.equals(expectDescription, anUpdatedCategory.getDescription())
                        && Objects.equals(expectIsActive, anUpdatedCategory.isActive())
                        && Objects.equals(expectedId, anUpdatedCategory.getId())
                        && Objects.nonNull(anUpdatedCategory.getUpdatedAt())
                        && Objects.nonNull(anUpdatedCategory.getCreatedAt())
                        && Objects.isNull(anUpdatedCategory.getDeletedAt())));
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

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                invalidName,
                expectDescription,
                expectIsActive);

        when(gateway.findById(eq(expectedId))).thenReturn(of(aCategory));

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
        assertTrue(aCategory.isActive());
        assertNull(aCategory.getDeletedAt());

        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(),
                expectedName, expectDescription, expectIsActive);

        when(gateway.findById(eq(expectedId))).thenReturn(of(aCategory));
        when(gateway.update(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(gateway).findById(eq(expectedId));
        verify(gateway).update(argThat(anUpdateCategory ->
                Objects.equals(expectedName, anUpdateCategory.getName())
                        && Objects.equals(expectDescription, anUpdateCategory.getDescription())
                        && Objects.equals(expectIsActive, anUpdateCategory.isActive())
                        && Objects.nonNull(anUpdateCategory.getId())
                        && Objects.nonNull(anUpdateCategory.getUpdatedAt())
                        && Objects.nonNull(anUpdateCategory.getCreatedAt())
                        && Objects.nonNull(anUpdateCategory.getDeletedAt())));
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

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectDescription, expectIsActive);

        when(gateway.findById(eq(expectedId))).thenReturn(of(aCategory));
        when(gateway.update(Mockito.any()))
                .thenThrow(new IllegalStateException("gateway error test"));

        final var notification = useCase.execute(aCommand).getLeft();

        assertNotNull(notification);
        assertEquals(expectErrorCount, notification.getErrors().size());
        assertEquals(expectErrorMessage, notification.firtsError().message());

        verify(gateway).findById(eq(expectedId));
        verify(gateway).update(argThat(anUpdateCategory ->
                Objects.equals(expectedName, anUpdateCategory.getName())
                        && Objects.equals(expectDescription, anUpdateCategory.getDescription())
                        && Objects.equals(expectIsActive, anUpdateCategory.isActive())
                        && Objects.nonNull(anUpdateCategory.getId())
                        && Objects.nonNull(anUpdateCategory.getUpdatedAt())
                        && Objects.nonNull(anUpdateCategory.getCreatedAt())
                        && Objects.isNull(anUpdateCategory.getDeletedAt())));
    }

    @Test
    void givenACommandWithInvalidId_whenCallsUpdateCategory_shouldReturnNotFound() {
        final var expectedName = "Filmes";
        final var expectDescription = "A categoria mais assistida";
        final var expectIsActive = true;
        final var nonExistentId = "123";
        final var expectedErrorMessage = format("Category with CategoryId %s was not fount", nonExistentId);
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCategoryCommand.with(nonExistentId,
                expectedName, expectDescription, expectIsActive);

        when(gateway.findById(CategoryId.from(nonExistentId)))
                .thenReturn(Optional.empty());

        final var actualException = assertThrows(DomainException.class,
                () -> useCase.execute(aCommand).get());

        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().getFirst().message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(gateway).findById(CategoryId.from(nonExistentId));
        verify(gateway, never()).update(any());
    }
}
