package io.github.catalogo.admin.domain;

public class AggregatedRoot<ID extends Identifier> extends Entity<ID>{

    protected AggregatedRoot(ID id) {
        super(id);
    }
}
