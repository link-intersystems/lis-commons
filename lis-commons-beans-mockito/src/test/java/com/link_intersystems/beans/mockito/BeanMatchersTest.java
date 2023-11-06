package com.link_intersystems.beans.mockito;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.link_intersystems.beans.mockito.BeanMatchers.propertiesEqual;
import static org.mockito.Mockito.*;

class BeanMatchersTest {

    interface PersonSetter {
        public void setPerson(Person person);
    }

    private Person johnDoe;
    private Person janeDoe;

    @BeforeEach
    void setUp() {
        PersonFixture personFixture = new PersonFixture();
        johnDoe = personFixture.getJohnDoe();
        janeDoe = personFixture.getJaneDoe();
    }

    @Test
    void testPropertiesEqual() {
        PersonSetter personSetter = mock(PersonSetter.class);

        personSetter.setPerson(johnDoe);

        Person johnDoeCopy = new Person();
        johnDoeCopy.setFirstname("John");
        johnDoeCopy.setLastname("Doe");
        johnDoeCopy.setAge(37);

        verify(personSetter, times(1)).setPerson(propertiesEqual(johnDoeCopy));
    }

    @Test
    void testPropertiesEqualExcludes() {
        PersonSetter personSetter = mock(PersonSetter.class);

        personSetter.setPerson(johnDoe);


        verify(personSetter, times(1)).setPerson(propertiesEqual(janeDoe, "firstname", "age"));
    }
}