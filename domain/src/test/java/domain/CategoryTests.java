package domain;

import io.github.catalogo.admin.domain.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryTests {

    @Test
    public void testNewCategory() {
        Assertions.assertNotNull(new Category());
    }
}