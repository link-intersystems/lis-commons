package com.link_intersystems.beans;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Parameter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ArgumentResolverTest {

    private ArgumentResolver argumentResolver;
    private Parameter[] parameters;

    @BeforeEach
    void setUp() {
        argumentResolver = spy(ArgumentResolver.class);
        parameters = new Parameter[]{mock(Parameter.class), mock(Parameter.class)};
    }

    @Test
    void canResolveArguments() {
        when(argumentResolver.canResolveArgument(parameters[0])).thenReturn(true);
        when(argumentResolver.canResolveArgument(parameters[1])).thenReturn(true);

        assertTrue(argumentResolver.canResolveArguments(parameters));
    }

    @Test
    void canNotResolveArguments() {
        when(argumentResolver.canResolveArgument(parameters[0])).thenReturn(true);
        when(argumentResolver.canResolveArgument(parameters[1])).thenReturn(false);

        assertFalse(argumentResolver.canResolveArguments(parameters));
    }

    @Test
    void resolveArguments() throws ArgumentResolveException {
        when(argumentResolver.resolveArgument(parameters[0])).thenReturn("a");
        when(argumentResolver.resolveArgument(parameters[1])).thenReturn("b");

        Object[] args = argumentResolver.resolveArguments(parameters);

        assertEquals("a", args[0]);
        assertEquals("b", args[1]);
    }

    @Test
    void argumentResolveException() throws ArgumentResolveException {
        when(argumentResolver.resolveArgument(parameters[0])).thenReturn("a");
        ArgumentResolveException are = new ArgumentResolveException();
        when(argumentResolver.resolveArgument(parameters[1])).thenThrow(are);

        ArgumentResolveException thrownException = assertThrows(ArgumentResolveException.class, () -> argumentResolver.resolveArguments(parameters));

        assertEquals(are, thrownException);
    }
}