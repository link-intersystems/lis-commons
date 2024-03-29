package com.link_intersystems.jdbc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static java.text.MessageFormat.format;
import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class TableReference {

    public static class Edge {

        private String tableName;
        private List<String> columns = new ArrayList<>();

        public Edge(String tableName, List<String> columns) {
            this.tableName = requireNonNull(tableName);
            if (columns.isEmpty()) {
                String msg = format("Edge for table ''{0}'' must define at least one column, " +
                        "but was zero.", tableName);
                throw new IllegalArgumentException(msg);
            }
            this.columns.addAll(columns);
        }

        public String getTableName() {
            return tableName;
        }

        public List<String> getColumns() {
            return Collections.unmodifiableList(columns);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Edge edge = (Edge) o;
            return Objects.equals(tableName, edge.tableName) &&
                    Objects.equals(columns, edge.columns);
        }

        @Override
        public int hashCode() {
            return Objects.hash(tableName, columns);
        }

        @Override
        public String toString() {
            return tableName + "(" + getColumns() + ")";
        }
    }

    public static enum Direction {
        NATURAL {
            @Override
            public TableReference.Edge getSourceEdge(TableReference dependency) {
                return dependency.getSourceEdge();
            }

            @Override
            public TableReference.Edge getTargetEdge(TableReference dependency) {
                return dependency.getTargetEdge();
            }
        }, REVERSED {
            @Override
            public TableReference.Edge getSourceEdge(TableReference reference) {
                return reference.getTargetEdge();
            }

            @Override
            public TableReference.Edge getTargetEdge(TableReference reference) {
                return reference.getSourceEdge();
            }
        };

        public abstract TableReference.Edge getSourceEdge(TableReference dependency);

        public abstract TableReference.Edge getTargetEdge(TableReference dependency);
    }

    public static TableReference of(ForeignKey foreignKey) {
        String name = foreignKey.getName();
        Edge sourceEdge = getSourceEdge(foreignKey);
        Edge targetEdge = getTargetEdge(foreignKey);
        return new TableReference(name, sourceEdge, targetEdge);
    }

    private static Edge getTargetEdge(ForeignKey foreignKey) {
        return getReferenceEdge(foreignKey, ForeignKeyEntry::getPkColumnDescription);
    }

    private static Edge getSourceEdge(ForeignKey foreignKey) {
        return getReferenceEdge(foreignKey, ForeignKeyEntry::getFkColumnDescription);
    }


    private static Edge getReferenceEdge(ForeignKey foreignKey, Function<ForeignKeyEntry, ColumnDescription> columnDescriptionGetter) {
        List<String> columns = new ArrayList<>();

        String tableName = null;

        for (ForeignKeyEntry entry : foreignKey) {
            ColumnDescription columnDescription = columnDescriptionGetter.apply(entry);
            columns.add(columnDescription.getColumnName());
            tableName = columnDescription.getTableName();
        }

        return new Edge(tableName, columns);
    }

    private final String name;
    private final Edge targetEdge;
    private final Edge sourceEdge;

    public TableReference(String name, Edge sourceEdge, Edge targetEdge) {
        this.name = requireNonNull(name);
        this.sourceEdge = requireNonNull(sourceEdge);
        this.targetEdge = requireNonNull(targetEdge);
    }

    /**
     * The name of this {@link TableReference}, usually the
     * foreign key name of the foreign key it was constructed from.
     *
     * @return the name of this {@link TableReference},
     */
    public String getName() {
        return name;
    }

    public Edge getSourceEdge() {
        return sourceEdge;
    }

    public Edge getTargetEdge() {
        return targetEdge;
    }

    /**
     * Creates a new {@link TableReference} that reverses this {@link TableReference}
     * by swapping the source and target edge.
     *
     * @return a new {@link TableReference} that reverses this {@link TableReference}
     * by swapping the source and target edge.
     */
    public TableReference reverse() {
        Direction direction = Direction.REVERSED;
        Edge sourceEdge = direction.getSourceEdge(this);
        Edge targetEdge = direction.getTargetEdge(this);
        return new TableReference(getName(), sourceEdge, targetEdge);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableReference that = (TableReference) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(targetEdge, that.targetEdge) &&
                Objects.equals(sourceEdge, that.sourceEdge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, targetEdge, sourceEdge);
    }

    @Override
    public String toString() {
        return name + "<" + sourceEdge + " -> " + targetEdge + ">";
    }
}
