package com.link_intersystems.sql.hibernate;

import org.hibernate.engine.jdbc.LobCreator;
import org.hibernate.engine.jdbc.NonContextualLobCreator;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

import java.util.TimeZone;

import static java.util.Objects.requireNonNull;

public class DefaultWrapperOptions implements WrapperOptions {

    public static final WrapperOptions INSTANCE = new DefaultWrapperOptions();

    private LobCreator lobCreator = NonContextualLobCreator.INSTANCE;
    private boolean useStreamForLobBinding;
    private TimeZone jdbcTimeZone = TimeZone.getDefault();

    @Override
    public boolean useStreamForLobBinding() {
        return useStreamForLobBinding;
    }

    public void setUseStreamForLobBinding(boolean useStreamForLobBinding) {
        this.useStreamForLobBinding = useStreamForLobBinding;
    }

    @Override
    public LobCreator getLobCreator() {
        return lobCreator;
    }

    public void setLobCreator(LobCreator lobCreator) {
        this.lobCreator = requireNonNull(lobCreator);
    }

    @Override
    public SqlTypeDescriptor remapSqlTypeDescriptor(SqlTypeDescriptor sqlTypeDescriptor) {
        return sqlTypeDescriptor;
    }

    @Override
    public TimeZone getJdbcTimeZone() {
        return jdbcTimeZone;
    }

    public void setJdbcTimeZone(TimeZone jdbcTimeZone) {
        this.jdbcTimeZone = requireNonNull(jdbcTimeZone);
    }
}