package io.github.catalogo.admin.infrastructure.configuration.category;

import io.github.catalogo.admin.application.category.create.CreateCategoryUseCase;
import io.github.catalogo.admin.application.category.create.DefaultCreateCategoryUseCase;
import io.github.catalogo.admin.application.category.delete.DefaultDeleteCategoryUseCase;
import io.github.catalogo.admin.application.category.delete.DeleteCategoryUseCase;
import io.github.catalogo.admin.application.category.retrieve.get.DefaultGetCategoryByIddUseCase;
import io.github.catalogo.admin.application.category.retrieve.get.GetCategoryByIddUseCase;
import io.github.catalogo.admin.application.category.retrieve.list.DefaultListCategoryUseCase;
import io.github.catalogo.admin.application.category.retrieve.list.ListCategoryUseCase;
import io.github.catalogo.admin.application.category.update.DefaultUpdateCategoryUseCase;
import io.github.catalogo.admin.application.category.update.UpdateCategoryUseCase;
import io.github.catalogo.admin.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    private final CategoryGateway gateway;

    public UseCaseConfig(final CategoryGateway aGateway) {
        this.gateway = aGateway;
    }

    @Bean
    public CreateCategoryUseCase createCategoryUseCase() {
        return new DefaultCreateCategoryUseCase(gateway);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase() {
        return new DefaultUpdateCategoryUseCase(gateway);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase() {
        return new DefaultDeleteCategoryUseCase(gateway);
    }

    @Bean
    public GetCategoryByIddUseCase getCategoryByIddUseCase() {
        return new DefaultGetCategoryByIddUseCase(gateway);
    }

    @Bean
    public ListCategoryUseCase listCategoryUseCase() {
        return new DefaultListCategoryUseCase(gateway);
    }
}
