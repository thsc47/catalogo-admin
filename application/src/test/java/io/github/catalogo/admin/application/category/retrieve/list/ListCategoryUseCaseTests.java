package io.github.catalogo.admin.application.category.retrieve.list;

import io.github.catalogo.admin.domain.category.Category;
import io.github.catalogo.admin.domain.category.CategoryGateway;
import io.github.catalogo.admin.domain.pagination.SearchQuery;
import io.github.catalogo.admin.domain.pagination.Pagination;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListCategoryUseCaseTests {

    @InjectMocks
    private DefaultListCategoryUseCase useCase;

    @Mock
    private CategoryGateway gateway;

    @Test
    void givenAValidQuery_whenCallListCategories_thenShouldReturnCategories() {
        final var aCategoryList = List.of(
                Category.newCategory("Filmes", "Descrição", true),
                Category.newCategory("Filmes1", "Descrição1", true),
                Category.newCategory("Filmes2", "Descrição2", false)
        );
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "ASC";

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var expectedPagination =
                new Pagination<>(expectedPage, expectedPerPage, aCategoryList.size(), aCategoryList);

        final var expectedItemsCount = 3;
        final var expectedResult = expectedPagination.map(CategoryListOutput::from);

        when(gateway.findAll(eq(aQuery)))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(aCategoryList.size(), actualResult.total());

        verify(gateway).findAll(eq(aQuery));
    }

    @Test
    void givenAValidQuery_whenHasNoResult_thenShouldReturnEmpty() {
        final var aCategoryList = List.<Category>of();
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "ASC";

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var expectedPagination =
                new Pagination<>(expectedPage, expectedPerPage, aCategoryList.size(), aCategoryList);

        final var expectedItemsCount = 0;
        final var expectedResult = expectedPagination.map(CategoryListOutput::from);

        when(gateway.findAll(eq(aQuery)))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(aCategoryList.size(), actualResult.total());

        verify(gateway).findAll(eq(aQuery));
    }

    @Test
    void givenAValidQuery_whenGatewayThrowsException_thenShouldReturnAnException() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "ASC";
        final var expectedErrorMessage = "Gateway Error";

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        when(gateway.findAll(eq(aQuery)))
                .thenThrow(new IllegalStateException("Gateway Error"));

        final var actualException = assertThrows(IllegalStateException.class, () -> useCase.execute(aQuery));

        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(gateway).findAll(eq(aQuery));
    }
}
