package com.link_intersystems.beans.mockito;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PropertiesMatcherTest {

    private PersonFixture personFixture;
    private PropertiesMatcher<Person> johnDoeMatcher;

    @BeforeEach
    void setUp() {
        personFixture = new PersonFixture();
        johnDoeMatcher = new PropertiesMatcher<>(personFixture.getJohnDoe());
    }

    @Test
    void matches() {
        assertTrue(johnDoeMatcher.matches(personFixture.getJohnDoe()));
    }

    @Test
    void wontMatch() {
        assertFalse(johnDoeMatcher.matches(personFixture.getJaneDoe()));
    }

    @Test
    void matchExcludes() {
        johnDoeMatcher = new PropertiesMatcher<>(personFixture.getJohnDoe(), "firstname", "age");

        assertTrue(johnDoeMatcher.matches(personFixture.getJaneDoe()));
    }
}