package io.github.catalogo.admin;

import io.github.catalogo.admin.infrastructure.configuration.ObjectMapperConfig;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Inherited
@WebMvcTest
@ActiveProfiles("test-integration")
@Target(ElementType.TYPE)
@Import({ObjectMapperConfig.class})
@Retention(RetentionPolicy.RUNTIME)
public @interface ControllerTest {

    @AliasFor(annotation = WebMvcTest.class, attribute = "controllers")
    Class<?>[] controllers() default {};
}
