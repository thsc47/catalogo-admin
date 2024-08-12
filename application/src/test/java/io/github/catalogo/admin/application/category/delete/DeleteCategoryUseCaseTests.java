package io.github.catalogo.admin.application.category.delete;

import io.github.catalogo.admin.application.category.update.UpdateCategoryCommand;
import io.github.catalogo.admin.domain.category.Category;
import io.github.catalogo.admin.domain.category.CategoryGateway;
import io.github.catalogo.admin.domain.category.CategoryId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DeleteCategoryUseCaseTests {

    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;

    @Mock
    private CategoryGateway gateway;

    @BeforeEach
    void cleanUp() {
        reset(gateway);
    }

    @Test
    void givenAValidCommandWIthAValidId_whenCallsDeleteCategory_shouldBeOk() {
        final var expectedName = "Filmes";
        final var expectDescription = "A categoria mais assistida";
        final var expectIsActive = true;
        final var aCategory = Category.newCategory(expectedName, expectDescription, expectIsActive);
        final var expectedId = aCategory.getId();

        doNothing().when(gateway).deleteById(eq(expectedId));

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(gateway).deleteById(eq(expectedId));
    }

    @Test
    void givenAValidCommandWIthAnInvalidId_whenCallsDeleteCategory_shouldReturnOk() {
        final var nonExistentId = CategoryId.from("123");

        doNothing().when(gateway).deleteById(eq(nonExistentId));

        assertDoesNotThrow(() -> useCase.execute(nonExistentId.getValue()));

        verify(gateway).deleteById(eq(nonExistentId));
    }

    @Test
    void givenAValidCommandWIthAValidId_whenGatewayThrowsAnRandomError_shouldReturnAnException() {
        final var nonExistentId = CategoryId.from("123");

        doThrow(new IllegalStateException("Gateway error")).when(gateway).deleteById(eq(nonExistentId));

        assertThrows(IllegalStateException.class, () -> useCase.execute(nonExistentId.getValue()));

        verify(gateway).deleteById(eq(nonExistentId));
    }
}
