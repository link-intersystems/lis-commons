package com.link_intersystems.net;

import org.junit.jupiter.api.Test;

import java.net.SocketException;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class MACTest {

    @Test
    void fromBytes() throws UnknownHostException, SocketException {
        MAC mac = new MAC(new byte[]{52, 46, -73, 11, 78, 96});

        assertEquals("34-2E-B7-0B-4E-60", mac.toString());
    }

    @Test
    void testEquals() throws UnknownHostException, SocketException {
        MAC mac1 = new MAC(new byte[]{52, 46, -73, 11, 78, 96});
        MAC mac2 = new MAC(new byte[]{52, 46, -73, 11, 78, 96});
        MAC mac3 = new MAC(new byte[]{52, 46, 73, 11, 78, 96});

        assertEquals(mac1, mac2);
        assertEquals(mac2, mac1);

        assertNotEquals(mac1, mac3);
        assertNotEquals(mac3, mac1);
    }

    @Test
    void testHashCode() throws UnknownHostException, SocketException {
        MAC mac1 = new MAC(new byte[]{52, 46, -73, 11, 78, 96});
        MAC mac2 = new MAC(new byte[]{52, 46, -73, 11, 78, 96});

        assertEquals(mac1.hashCode(), mac2.hashCode());
    }
}