package com.link_intersystems.jdbc.test.db.sakila;

import com.link_intersystems.jdbc.test.db.h2.H2Config;
import com.link_intersystems.jdbc.test.db.h2.H2Extension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@ExtendWith(H2Extension.class)
@H2Config(databaseFactory = SakilaTinyDatabaseFactory.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SakilaTinyExtension {
}
