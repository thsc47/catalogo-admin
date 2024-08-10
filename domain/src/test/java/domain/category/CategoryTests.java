package domain.category;

import io.github.catalogo.admin.domain.category.Category;
import io.github.catalogo.admin.domain.exceptions.DomainException;
import io.github.catalogo.admin.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

public class CategoryTests {

    @Test
    public void givenAValidParam_whenCallANewCategory_thenInstantiateACategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertNotNull(aCategory);
        Assertions.assertNotNull(aCategory.getId());
        Assertions.assertEquals(expectedDescription, aCategory.getDescription());
        Assertions.assertEquals(expectedName, aCategory.getName());
        Assertions.assertEquals(expectedIsActive, aCategory.isActive());
        Assertions.assertNotNull(aCategory.getCreatedAt());
        Assertions.assertNotNull(aCategory.getUpdatedAt());
        Assertions.assertNull(aCategory.getDeletedAt());

    }

    @Test
    public void givenAnInvalidNullName_whenCallANewCategoryAndValidate_thenShouldReceiveAnError() {
        final String nullName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualCategory = Category.newCategory(nullName, expectedDescription, expectedIsActive);
        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidEmptyName_whenCallANewCategoryAndValidate_thenShouldReceiveAnError() {
        final String blankName = " ";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be blank";

        final var actualCategory = Category.newCategory(blankName, expectedDescription, expectedIsActive);
        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNameLengthLessThanThree_whenCallANewCategoryAndValidate_thenShouldReceiveAnError() {
        final String blankName = "Fi ";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should be between 3 and 255 characters";

        final var actualCategory = Category.newCategory(blankName, expectedDescription, expectedIsActive);
        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNameLengthAbove255_whenCallANewCategoryAndValidate_thenShouldReceiveAnError() {
        final String blankName = """
                No mundo atual, a percepção das dificuldades aponta para a melhoria do retorno esperado a longo prazo.
                Não obstante, o comprometimento entre as equipes acarreta um processo de reformulação e modernização
                do orçamento setorial.
                Caros amigos, o surgimento do comércio virtual não pode mais se dissociar do remanejamento 
                dos quadros funcionais.
                Evidentemente, a consolidação das estruturas cumpre um papel essencial na formulação do levantamento 
                das variáveis envolvidas.
                """;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should be between 3 and 255 characters";

        final var actualCategory = Category.newCategory(blankName, expectedDescription, expectedIsActive);
        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidEmptyDescription_whenCallANewCategoryAndValidate_thenShouldReceiveAnError() {
        final var expectedName = "Filmes";
        final var blankDescription = " ";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, blankDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertNotNull(aCategory);
        Assertions.assertNotNull(aCategory.getId());
        Assertions.assertEquals(blankDescription, aCategory.getDescription());
        Assertions.assertEquals(expectedName, aCategory.getName());
        Assertions.assertEquals(expectedIsActive, aCategory.isActive());
        Assertions.assertNotNull(aCategory.getCreatedAt());
        Assertions.assertNotNull(aCategory.getUpdatedAt());
        Assertions.assertNull(aCategory.getDeletedAt());
    }

    @Test
    public void givenAValidFalseIsActive_whenCallANewCategoryAndValidate_thenShouldReceiveAnError() {
        final var expectedName = "Filmes";
        final var blankDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var aCategory = Category.newCategory(expectedName, blankDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertNotNull(aCategory);
        Assertions.assertNotNull(aCategory.getId());
        Assertions.assertEquals(blankDescription, aCategory.getDescription());
        Assertions.assertEquals(expectedName, aCategory.getName());
        Assertions.assertEquals(expectedIsActive, aCategory.isActive());
        Assertions.assertNotNull(aCategory.getCreatedAt());
        Assertions.assertNotNull(aCategory.getUpdatedAt());
        Assertions.assertNotNull(aCategory.getDeletedAt());
    }

    @Test
    public void givenAValidActiveCategory_whenCallDeactivate_thenReturnCategoryDeactivated() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertNull(aCategory.getDeletedAt());
        Assertions.assertTrue(aCategory.isActive());

        final var actualCategory = aCategory.deactivate();
        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertFalse(actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidInactiveCategory_whenCallActivate_thenReturnCategoryActivated() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        final Instant updatedAt = aCategory.getUpdatedAt();

        Assertions.assertNotNull(aCategory.getDeletedAt());
        Assertions.assertFalse(aCategory.isActive());

        final var actualCategory = aCategory.activate();
        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertTrue(actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualCategory.getDeletedAt());
    }
}