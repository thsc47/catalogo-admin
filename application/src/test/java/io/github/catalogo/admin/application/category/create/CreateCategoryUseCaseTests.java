package io.github.catalogo.admin.application.category.create;

import io.github.catalogo.admin.domain.category.Category;
import io.github.catalogo.admin.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreateCategoryUseCaseTests {

    @Test
    void givenAValidCommando_whenCallsCreateCategory_shouldReturnCategoryId() {
        final var expectedName = "Filmes";
        final var expectDescription = "A categoria mais assistida";
        final var expectIsActive = true;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectDescription, expectIsActive);

        final var gateway = Mockito.mock(CategoryGateway.class);
        when(gateway.create(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var useCase = new DefaultCreateCategoryUseCase(gateway);

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
}
