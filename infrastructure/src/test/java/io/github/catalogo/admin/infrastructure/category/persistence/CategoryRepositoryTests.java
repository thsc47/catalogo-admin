package io.github.catalogo.admin.infrastructure.category.persistence;

import io.github.catalogo.admin.domain.category.Category;
import io.github.catalogo.admin.MySQLGatewayTest;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

@MySQLGatewayTest
public class CategoryRepositoryTests {

    @Autowired
    private CategoryRepository repository;

    @Test
    void gianAnInvalidNullName_whenCallSave_shouldReturnAnError() {
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var anEntity = CategoryJpaEntity.from(aCategory);
        final var expectedProperty = "name";
        final var expectedMessage =
                "not-null property references a null or transient value : io.github.catalogo.admin.infrastructure.category.persistence.CategoryJpaEntity.name";
        anEntity.setName(null);

        final var actualException =
                assertThrows(DataIntegrityViolationException.class, () -> repository.save(anEntity));
        final var actualCause =
                assertInstanceOf(PropertyValueException.class, actualException.getCause());
        assertEquals(expectedProperty, actualCause.getPropertyName());
        assertEquals(expectedMessage, actualCause.getMessage());

    }
}
