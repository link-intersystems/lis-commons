package com.link_intersystems.beans;

import com.link_intersystems.beans.BeanMapDecorator.IndexedValue;
import com.link_intersystems.beans.java.SomeBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.IntrospectionException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BeanMapDecoratorTest {

    private BeanMapDecorator beanMapDecorator;
    private SomeBean someBean;

    @BeforeEach
    public void setupBeanMap() {
        someBean = new SomeBean();


        BeansFactory beansFactory = BeansFactory.getDefault();
        Bean<SomeBean> containerJavaBean = beansFactory.createBeanClass(SomeBean.class, Object.class).getBeanFromInstance(someBean);
        beanMapDecorator = new BeanMapDecorator(containerJavaBean);
    }

    @Test
    void containsKey() {
        assertFalse(beanMapDecorator.containsKey(null));
        assertFalse(beanMapDecorator.containsKey("unknownProperty"));
        assertTrue(beanMapDecorator.containsKey("stringProperty"));
    }

    @Test
    void isEmpty() {
        assertFalse(beanMapDecorator.isEmpty());
    }

    @Test
    void size() {
        int size = beanMapDecorator.size();
        assertEquals(SomeBean.PROPERTY_NAMES.size(), size);
    }

    @Test
    void getNull() {
        Object value = beanMapDecorator.get(null);
        Assertions.assertNull(value);
    }

    @Test
    void get() {
        someBean.setStringProperty("SomeStringValue");

        Object value = beanMapDecorator.get("stringProperty");
        assertEquals("SomeStringValue", value);
    }

    @Test
    void getUnknownProperty() {
        someBean.setStringProperty("SomeStringValue");

        Object value = beanMapDecorator.get("unknownProperty");

        assertNull(value);
    }

    @Test
    void getArrayPropertyNotIndexed() {
        String[] values = {"A", "B", "C"};
        someBean.setArrayPropertyNoIndexAccess(values);

        String[] indexedValue = (String[]) beanMapDecorator.get("arrayPropertyNoIndexAccess");
        assertNotNull(indexedValue);

        assertArrayEquals(values, indexedValue);
    }

    @Test
    void getIndexValue() {
        someBean.setStringArrayProperty(new String[]{"A", "B", "C"});

        IndexedValue indexedValue = (IndexedValue) beanMapDecorator.get("stringArrayProperty");
        assertNotNull(indexedValue);

        Object element = indexedValue.getElement(0);
        assertEquals("A", element);
    }

    @Test
    void putNullProperty() {
        assertThrows(IllegalArgumentException.class, () -> beanMapDecorator.put(null, "SomeValue"));
    }

    @Test
    void putUnknownProperty() {
        assertThrows(NoSuchPropertyException.class, () -> beanMapDecorator.put("unknownProperty", "SomeValue"));
    }

    @Test
    void put() {
        beanMapDecorator.put("stringProperty", "SomeValue");
        assertEquals("SomeValue", someBean.getStringProperty());
    }

    @Test
    void putIndexedValue() {
        someBean.setStringArrayProperty(new String[]{"A", "B", "C"});

        beanMapDecorator.put("stringArrayProperty", BeanMapDecorator.indexSetter(2, "F"));

        IndexedValue indexedValue = (IndexedValue) beanMapDecorator.get("stringArrayProperty");
        assertEquals("F", indexedValue.getElement(2));
    }

    @Test
    void putIndexedPropertyNoIndexValueSetter() {
        someBean.setStringArrayProperty(new String[]{"A", "B", "C"});

        assertThrows(IllegalArgumentException.class, () -> beanMapDecorator.put("stringArrayProperty", "F"));
    }

    @Test
    void putArrayPropertyNotIndexed() {
        String[] values = {"A", "B", "C"};

        beanMapDecorator.put("arrayPropertyNoIndexAccess", values);

        assertArrayEquals(values, someBean.getArrayPropertyNoIndexAccess());
    }

    @Test
    void putAll() {
        someBean.setStringArrayProperty(new String[]{"A", "B", "C"});

        Map<String, Object> values = new HashMap<>();
        values.put("stringProperty", "putAll");
        values.put("stringArrayProperty", BeanMapDecorator.indexSetter(1, "F"));
        values.put("arrayPropertyNoIndexAccess", new String[]{"D", "E", "F"});

        beanMapDecorator.putAll(values);

        assertEquals("putAll", someBean.getStringProperty());
        assertArrayEquals(new String[]{"A", "F", "C"}, someBean.getStringArrayProperty());
        assertArrayEquals(new String[]{"D", "E", "F"}, someBean.getArrayPropertyNoIndexAccess());
    }

    @Test
    void entrySet() {
        java.util.List<String> expectedPropertyNames = new ArrayList<>(SomeBean.PROPERTY_NAMES);

        Set<Map.Entry<String, Object>> entries = beanMapDecorator.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            assertTrue(expectedPropertyNames.remove(entry.getKey()), entry.getKey());
        }
    }


    @Test
    void entryPutValue() {
        Set<Map.Entry<String, Object>> entries = beanMapDecorator.entrySet();
        Map.Entry<String, Object> entry = entries.stream().filter(e -> e.getKey().equals("stringProperty"))
                .findFirst().orElseThrow(() -> new IllegalStateException("no property named stringProperty"));

        entry.setValue("entryPutValue");

        assertEquals("entryPutValue", someBean.getStringProperty());
    }

    @Test
    void entryGetValue() {
        someBean.setStringProperty("entryGetValue");

        Set<Map.Entry<String, Object>> entries = beanMapDecorator.entrySet();
        Map.Entry<String, Object> entry = entries.stream().filter(e -> e.getKey().equals("stringProperty"))
                .findFirst().orElseThrow(() -> new IllegalStateException("no property named stringProperty"));

        assertEquals("entryGetValue", entry.getValue());
    }

    @Test
    void containsValue() {
        assertFalse(beanMapDecorator.containsValue("ABC"));

        someBean.setStringProperty("ABC");

        assertTrue(beanMapDecorator.containsValue("ABC"));
    }

    @Test
    void keySet() {
        java.util.List<String> expectedPropertyNames = new ArrayList<>(SomeBean.PROPERTY_NAMES);

        Set<String> propertyNames = beanMapDecorator.keySet();
        for (String propertyName : propertyNames) {
            assertTrue(expectedPropertyNames.remove(propertyName), propertyName);
        }
    }

    @Test
    void values() {
        Collection<Object> values = beanMapDecorator.values();

        assertEquals(SomeBean.PROPERTY_NAMES.size(), values.size());
    }

}
