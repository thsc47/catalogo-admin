package io.github.catalogo.admin;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import java.lang.annotation.*;

import static org.springframework.context.annotation.FilterType.REGEX;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@DataJpaTest
@ActiveProfiles("test-integration")
@ComponentScan(
        basePackages = "com.fullcycle.admin.catalogo",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".[MySQLGateway]")
        }
)
@ExtendWith(CleanUpExtension.class)
public @interface MySQLGatewayTest {}
