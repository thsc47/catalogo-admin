package io.github.catalogo.admin.infrastructure.category;

import io.github.catalogo.admin.infrastructure.MySQLGatewayTest;
import io.github.catalogo.admin.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MySQLGatewayTest
public class CategoryMySQLTests {

    @Autowired
    private CategoryMySQLGateway mysqlGateway;

    @Autowired
    private CategoryRepository repository;

    @Test
    void testInjectDependacies() {
        Assertions.assertNotNull(repository);
        Assertions.assertNotNull(mysqlGateway);
    }
}
