package com.link_intersystems.beans;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class BeanEventTypeListTest {

    private BeanEventType beanEventType1;
    private BeanEventType beanEventType2;
    private BeanEventTypeList beanEventTypesList;

    @BeforeEach
    void setUp() {
        List<BeanEventType> beanEventTypes = new ArrayList<>();

        beanEventType1 = Mockito.mock(BeanEventType.class);
        beanEventType2 = Mockito.mock(BeanEventType.class);

        beanEventTypes.add(beanEventType1);
        beanEventTypes.add(beanEventType2);

        beanEventTypesList = new BeanEventTypeList(beanEventTypes);
    }

    @Test
    void getByName() {
        when(beanEventType1.getName()).thenReturn("propertyChange");
        when(beanEventType2.getName()).thenReturn("change");

        BeanEventType propertyChangeEvent = beanEventTypesList.getByName("change");

        assertEquals(beanEventType2, propertyChangeEvent);
    }

    @Test
    void size() {
        assertEquals(2, beanEventTypesList.size());
    }
}