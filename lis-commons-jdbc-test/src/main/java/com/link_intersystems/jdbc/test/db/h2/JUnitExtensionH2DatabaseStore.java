package com.link_intersystems.jdbc.test.db.h2;

import org.junit.jupiter.api.extension.ExtensionContext;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class JUnitExtensionH2DatabaseStore implements H2DatabaseStore {

    private ExtensionContext.Namespace namespace = ExtensionContext.Namespace.create(JUnitExtensionH2DatabaseStore.class);

    private ExtensionContext extensionContext;

    public JUnitExtensionH2DatabaseStore(ExtensionContext extensionContext) {
        this.extensionContext = requireNonNull(extensionContext);
    }

    @Override
    public void put(H2Database h2Database) {
        ExtensionContext.Store store = extensionContext.getStore(namespace);
        store.put("db", h2Database);
    }

    @Override
    public H2Database get() {
        ExtensionContext.Store store = extensionContext.getStore(namespace);
        return (H2Database) store.get("db");
    }

    @Override
    public void remove() {
        ExtensionContext.Store store = extensionContext.getStore(namespace);
        store.remove("db");
    }
}
