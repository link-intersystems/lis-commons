package com.link_intersystems.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class OrPredicateTest {

    private Predicate predicate1;
    private Predicate predicate2;

    private OrPredicate<Object> orPredicate;

    @BeforeEach
    void setUp() {
        predicate1 = mock(Predicate.class);
        predicate2 = mock(Predicate.class);

        orPredicate = new OrPredicate<>(predicate1, predicate2);
    }

    @Test
    void testTrue() {
        when(predicate1.test(any())).thenReturn(true).thenReturn(true).thenReturn(false);
        when(predicate2.test(any())).thenReturn(true).thenReturn(false).thenReturn(true);

        assertTrue(orPredicate.test(""));
        assertTrue(orPredicate.test(""));
        assertTrue(orPredicate.test(""));
    }

    @Test
    void testFalse() {
        when(predicate1.test(any())).thenReturn(false);
        when(predicate2.test(any())).thenReturn(false);

        assertFalse(orPredicate.test(""));
    }

}