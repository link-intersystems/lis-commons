package com.link_intersystems.net.wol;

import org.junit.jupiter.api.Assertions;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class ByteArrayAssertion {

    private byte[] actual;

    public ByteArrayAssertion(byte[] actual) {
        this.actual = actual;
    }

    public void assertEquals(int pos, int... expectedBytes) {
        assertEquals(pos, bytes(expectedBytes));
    }

    private static byte[] bytes(int... bytes) {
        byte[] result = new byte[bytes.length];

        for (int i = 0; i < bytes.length; i++) {
            result[i] = (byte) bytes[i];
        }

        return result;
    }

    public void assertEquals(int pos, byte[] expectedBytes) {

        for (int i = pos; i < pos + expectedBytes.length; i++) {
            Assertions.assertEquals(expectedBytes[i - pos], actual[i]);
        }
    }
}
