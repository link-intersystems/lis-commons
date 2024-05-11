package com.link_intersystems.jdbc;

import java.sql.Connection;

class SuppliedConnectionDelegateTest extends AbstractConnectionDelegateTest {

    @Override
    protected AbstractConnectionDelegate createConnectionDelegate(Connection targetConnection) {
        return new SuppliedConnectionDelegate(targetConnection);
    }

}