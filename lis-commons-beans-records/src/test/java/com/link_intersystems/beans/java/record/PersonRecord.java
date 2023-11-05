package com.link_intersystems.beans.java.record;

public record PersonRecord(String firstname, String lastname) {

    PersonRecord() {
        this("unknown", "unknown");
    }
}
