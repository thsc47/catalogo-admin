package io.github.catalogo.admin.application.category.create;

public record CreateCategoryCommand(
        String name,
        String description,
        boolean isActive
) {
    public static CreateCategoryCommand with(final String aname,
                                             final String aDescription,
                                             final boolean isActive) {
        return new CreateCategoryCommand(aname, aDescription, isActive);
    }
}
