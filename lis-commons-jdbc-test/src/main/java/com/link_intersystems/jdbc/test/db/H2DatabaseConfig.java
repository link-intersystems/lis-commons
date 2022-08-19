package com.link_intersystems.jdbc.test.db;

import com.link_intersystems.jdbc.test.db.setup.DBSetup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface H2DatabaseConfig {

    Class<? extends H2DatabaseFactory> databaseFactory() default DefaultH2DatabaseFactory.class;
    Class<? extends DBSetup> databaseSetup() default NoDBSetup.class;
}
