package com.link_intersystems.test;

import org.junit.jupiter.api.Tag;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *  @author - René Link {@literal <rene.link@link-intersystems.com>}
 */
@Retention(RUNTIME)
@Target(TYPE)
@Tag("UnitTest")
public @interface UnitTest {
}
