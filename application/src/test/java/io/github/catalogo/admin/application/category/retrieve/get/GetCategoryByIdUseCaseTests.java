package io.github.catalogo.admin.application.category.retrieve.get;

import io.github.catalogo.admin.application.UseCaseTest;
import io.github.catalogo.admin.domain.category.Category;
import io.github.catalogo.admin.domain.category.CategoryGateway;
import io.github.catalogo.admin.domain.category.CategoryId;
import io.github.catalogo.admin.domain.exceptions.DomainException;
import io.github.catalogo.admin.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class GetCategoryByIdUseCaseTests extends UseCaseTest {

    @InjectMocks
    private DefaultGetCategoryByIddUseCase useCase;

    @Mock
    private CategoryGateway gateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    void givenAValidId_whenCallsGetById_shouldACategory() {
        final var expectedName = "Filmes";
        final var expectDescription = "A categoria mais assistida";
        final var expectIsActive = true;
        final var aCategory = Category.newCategory(expectedName, expectDescription, expectIsActive);
        final var expectedId = aCategory.getId();

        when(gateway.findById(eq(expectedId))).thenReturn(of(aCategory));

        final var actualOutput = useCase.execute(expectedId.getValue());

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());
        assertEquals(expectedName, actualOutput.name());
        assertEquals(expectDescription, actualOutput.description());
        assertEquals(expectIsActive, actualOutput.isActive());
        assertNotNull(actualOutput.createdAt());
        assertNotNull(actualOutput.updatedAt());
        assertNull(actualOutput.deletedAt());

        verify(gateway).findById(eq(expectedId));
    }

    @Test
    void givenAnInvalidId_whenCallsGetByID_shouldReturnNotFound() {
        final var expectedId = CategoryId.from("123");
        final var expectErrorMessage = format("Category with id %s was not found", expectedId.getValue());

        when(gateway.findById(eq(expectedId))).thenReturn(empty());

        final var expectedException = assertThrows(NotFoundException.class, () ->
                useCase.execute(expectedId.getValue()));

        assertNotNull(expectedException);
        assertEquals(expectErrorMessage, expectedException.getMessage());

        verify(gateway).findById(eq(expectedId));
    }

    @Test
    void givenAValidId_whenGatewayThrowsAnRandomError_shouldReturnAnException() {
        final var nonExistentId = CategoryId.from("123");
        final var expectErrorMessage = "Gateway Error";

        when(gateway.findById(eq(nonExistentId))).thenThrow(new IllegalStateException(expectErrorMessage));

        final var expectedException = assertThrows(IllegalStateException.class, () ->
                useCase.execute(nonExistentId.getValue()));

        assertNotNull(expectedException);
        assertEquals(expectErrorMessage, expectedException.getMessage());

        verify(gateway).findById(eq(nonExistentId));
    }
}
