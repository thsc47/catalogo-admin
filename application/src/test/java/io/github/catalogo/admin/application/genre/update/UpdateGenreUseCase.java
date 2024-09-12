package io.github.catalogo.admin.application.genre.update;

import io.github.catalogo.admin.domain.category.CategoryGateway;
import io.github.catalogo.admin.domain.category.CategoryId;
import io.github.catalogo.admin.domain.genre.Genre;
import io.github.catalogo.admin.domain.genre.GenreGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateGenreUseCase {

    @InjectMocks
    private DefaultUpdateGenreUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    @Test
    public void givenAValidCommand_whenCallsUpdateGenre_shouldReturnGenreId() {
        // given
        final var aGenre = Genre.newGenre("acao", true);

        final var expectedId = aGenre.getId();
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryId>of();

        final var aCommand = UpdateGenreCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                expectedCategories
        );

        when(genreGateway.findById(any()))
                .thenReturn(Optional.of(Genre.with(aGenre)));

        when(genreGateway.update(any()))
                .thenAnswer(returnsFirstArg());
        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());

        verify(genreGateway).findById(eq(expectedId));

        verify(genreGateway)).update(argThat(aUpdatedGenre ->
                Objects.equals(expectedId, aUpdatedGenre.getId())
                        && Objects.equals(expectedName, aUpdatedGenre.getName())
                        && Objects.equals(expectedIsActive, aUpdatedGenre.isActive())
                        && Objects.equals(expectedCategories, aUpdatedGenre.getCategories())
                        && Objects.equals(aGenre.getCreatedAt(), aUpdatedGenre.getCreatedAt())
                        && aGenre.getUpdatedAt().isBefore(aUpdatedGenre.getUpdatedAt())
                        && Objects.isNull(aUpdatedGenre.getDeletedAt())
        ));
    }
}
