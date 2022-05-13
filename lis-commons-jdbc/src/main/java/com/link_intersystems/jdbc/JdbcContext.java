package com.link_intersystems.jdbc;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class JdbcContext {
    public static class Builder {
        private String catalog;
        private String schema;

        public Builder setCatalog(String catalog) {
            this.catalog = catalog;
            return this;
        }

        public Builder setSchema(String schema) {
            this.schema = schema;
            return this;
        }

        public JdbcContext build() {
            return new JdbcContext(catalog, schema);
        }
    }

    private String catalog;
    private String schema;

    private JdbcContext(String catalog, String schema) {
        this.catalog = catalog;
        this.schema = schema;
    }

    public String getCatalog() {
        return catalog;
    }

    public String getSchema() {
        return schema;
    }
}
