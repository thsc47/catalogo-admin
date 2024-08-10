package io.github.catalogo.admin.domain;

public abstract class AggregatedRoot<ID extends Identifier> extends Entity<ID>{

    protected AggregatedRoot(ID id) {
        super(id);
    }
}
