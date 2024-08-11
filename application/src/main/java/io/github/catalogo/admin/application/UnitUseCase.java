package io.github.catalogo.admin.application;

public abstract class UnitUseCase<IN> {
    public abstract void execute(IN anIN);
}
