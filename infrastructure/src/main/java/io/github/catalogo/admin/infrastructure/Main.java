package io.github.catalogo.admin.infrastructure;

import io.github.catalogo.admin.application.UseCase;

public class Main {
    public static void main(String[] args) {
        System.out.println(new UseCase().execute());
    }
}