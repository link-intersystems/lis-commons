package com.link_intersystems.util.adapter;

public class TestAdapterClass {
    private Object adaptableObject;

    public TestAdapterClass(Object adaptableObject) {
        this.adaptableObject = adaptableObject;
    }

    public Object getAdaptableObject() {
        return adaptableObject;
    }
}
