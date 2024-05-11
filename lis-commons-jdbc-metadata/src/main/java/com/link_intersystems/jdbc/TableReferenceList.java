package com.link_intersystems.jdbc;

import java.util.AbstractList;
import java.util.List;
import java.util.Objects;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class TableReferenceList extends AbstractList<TableReference> {

    private List<TableReference> references;

    public TableReferenceList(List<TableReference> references) {
        this.references = Objects.requireNonNull(references);
    }

    @Override
    public TableReference get(int index) {
        return references.get(index);
    }

    @Override
    public int size() {
        return references.size();
    }

    public TableReference getByName(String name) {
        return stream().filter(tr -> tr.getName().equals(name)).findFirst().orElse(null);
    }
}
