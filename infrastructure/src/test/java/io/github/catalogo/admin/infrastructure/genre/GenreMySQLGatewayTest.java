package io.github.catalogo.admin.infrastructure.genre;

import io.github.catalogo.admin.MySQLGatewayTest;
import io.github.catalogo.admin.infrastructure.category.CategoryMySQLGateway;
import io.github.catalogo.admin.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.wildfly.common.Assert.assertNotNull;

@MySQLGatewayTest
public class GenreMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryGateway;

    @Autowired
    private GenreMySQLGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void testDependenciesInjected() {
        assertNotNull(categoryGateway);
        assertNotNull(genreGateway);
        assertNotNull(genreRepository);
    }
}
