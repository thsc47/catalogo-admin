package io.github.catalogo.admin;

import io.github.catalogo.admin.infrastructure.configuration.WebServerConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@AutoConfigureMockMvc
@ActiveProfiles("test-e2e")
@SpringBootTest(classes = {WebServerConfig.class})
@ExtendWith(CleanUpExtension.class)
public @interface E2ETest {

}
