package io.github.catalogo.admin.application;

import io.github.catalogo.admin.domain.category.Category;

public class UseCase {
    public Category execute() {
        return Category.newCategory("Filmes", "A categoria mais assistida", true);
    }
}