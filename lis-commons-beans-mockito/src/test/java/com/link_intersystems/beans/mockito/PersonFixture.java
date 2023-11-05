package com.link_intersystems.beans.mockito;

public class PersonFixture {

    public Person getJohnDoe() {
        Person johnDoe = new Person();

        johnDoe.setFirstname("John");
        johnDoe.setLastname("Doe");
        johnDoe.setAge(37);

        return johnDoe;
    }

    public Person getJaneDoe() {
        Person janeDoe = new Person();

        janeDoe.setFirstname("Jane");
        janeDoe.setLastname("Doe");
        janeDoe.setAge(35);

        return janeDoe;
    }
}
