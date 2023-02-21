package com.link_intersystems.swing.action;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AsycWorkLifecycleTest {

    @Test
    void nullInstance() {
        AsycWorkLifecycle<Object, Object> nullInstance = AsycWorkLifecycle.nullInstance();

        nullInstance.prepareForExecution();
        nullInstance.intermediateResults(Arrays.asList());
        nullInstance.done("");

        RuntimeException runtimeException = new RuntimeException();
        RuntimeException thrownRuntimeException = assertThrows(RuntimeException.class, () -> nullInstance.failed(new ExecutionException(runtimeException)));
        assertEquals(runtimeException, thrownRuntimeException);


        Exception checkedException = new Exception();
        RuntimeException thrownCheckedException = assertThrows(RuntimeException.class, () -> nullInstance.failed(new ExecutionException(checkedException)));
        assertEquals(checkedException, thrownCheckedException.getCause());


        InterruptedException interruptedException = new InterruptedException();
        RuntimeException thrownInterruptedException = assertThrows(RuntimeException.class, () -> nullInstance.interrupted(interruptedException));
        assertEquals(checkedException, thrownInterruptedException.getCause());
    }
}