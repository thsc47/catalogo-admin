package io.github.catalogo.admin.infrastructure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ApplicationTests {

    @Test
    public void testAppliticationRun() {
        Assertions.assertNotNull(new Main());
        Main.main(new String[]{});
    }
}
