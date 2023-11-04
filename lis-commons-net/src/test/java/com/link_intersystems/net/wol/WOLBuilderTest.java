package com.link_intersystems.net.wol;

import com.link_intersystems.net.MAC;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.stubbing.Answer;

import java.net.*;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WOLBuilderTest {

    private MockedStatic<NetworkInterface> networkInterfaceMocked;
    private NetworkInterface loopbackInterface;
    private WOLBuilder wolBuilder;
    private MAC macMock;

    @BeforeEach
    void setUp() throws SocketException {
        networkInterfaceMocked = mockStatic(NetworkInterface.class);
        loopbackInterface = mock(NetworkInterface.class);
        when(loopbackInterface.isLoopback()).thenReturn(true);

        NetworkInterface networkInterface = mockNetworkInterface("192.168.0.1");
        setNetworkInterfaces(loopbackInterface, networkInterface);

        macMock = mock(MAC.class);
        wolBuilder = new WOLBuilder(macMock);
    }

    private NetworkInterface mockNetworkInterface(String ipAddress) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            NetworkInterface networkInterface = mock(NetworkInterface.class);
            InterfaceAddress interfaceAddress = mock(InterfaceAddress.class);
            when(interfaceAddress.getAddress()).thenReturn(inetAddress);
            byte[] broadcast = inetAddress.getAddress();
            broadcast[3] = (byte) 0xFF;
            InetAddress broadcastAddress = InetAddress.getByAddress(broadcast);
            when(interfaceAddress.getBroadcast()).thenReturn(broadcastAddress);
            when(networkInterface.getInterfaceAddresses()).thenReturn(Arrays.asList(interfaceAddress));
            return networkInterface;
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    private void setNetworkInterfaces(NetworkInterface... networkInterface) {
        networkInterfaceMocked.when(NetworkInterface::networkInterfaces).thenAnswer((Answer<Stream<NetworkInterface>>) invocation -> Arrays.stream(networkInterface));
    }

    @AfterEach
    void tearDown() {
        networkInterfaceMocked.close();
    }

    @Test
    void networkInterfacesThrowsException() {
        networkInterfaceMocked.when(NetworkInterface::networkInterfaces).thenThrow(new SocketException());
        assertThrows(RuntimeException.class, wolBuilder::build);
    }

    @Test
    void isLoopbackThrowsException() throws SocketException {
        when(loopbackInterface.isLoopback()).thenThrow(new SocketException());
        assertThrows(RuntimeException.class, wolBuilder::build);
    }


    @Test
    void defaultOptions() {
        WOL wol = wolBuilder.build();

        assertEquals(macMock, wol.getMac());

        InetSocketAddress targetSocketAddress = wol.getTargetSocketAddress();
        assertNotNull(targetSocketAddress);

        assertEquals("192.168.0.255", targetSocketAddress.getAddress().getHostAddress());
        assertEquals(WOL.DEFAULT_PORT, targetSocketAddress.getPort());
    }

    @Test
    void withDefaultBroadcastAddress() {
        WOL wol = wolBuilder.withDefaultBroadcastAddress().build();

        assertEquals(macMock, wol.getMac());

        InetSocketAddress targetSocketAddress = wol.getTargetSocketAddress();
        assertNotNull(targetSocketAddress);

        assertEquals("192.168.0.255", targetSocketAddress.getAddress().getHostAddress());
        assertEquals(WOL.DEFAULT_PORT, targetSocketAddress.getPort());
    }

    @Test
    void withBroadcastAddress() {
        NetworkInterface networkInterface = mockNetworkInterface("192.168.1.1");
        WOL wol = wolBuilder.withBroadcastAddress(networkInterface).build();

        assertEquals(macMock, wol.getMac());

        InetSocketAddress targetSocketAddress = wol.getTargetSocketAddress();
        assertNotNull(targetSocketAddress);

        assertEquals("192.168.1.255", targetSocketAddress.getAddress().getHostAddress());
        assertEquals(WOL.DEFAULT_PORT, targetSocketAddress.getPort());
    }

    @Test
    void selectorDoesNotSelectAnyInterface() {
        assertThrows(IllegalStateException.class, () -> wolBuilder.withBroadcastAddress(nic -> false).build());
    }

    @Test
    void withBroadcastAddressSelector() {
        WOL wol = wolBuilder.withBroadcastAddress(nic -> {
            try {
                return !nic.isLoopback();
            } catch (SocketException e) {
                throw new RuntimeException(e);
            }
        }).build();

        assertEquals(macMock, wol.getMac());

        InetSocketAddress targetSocketAddress = wol.getTargetSocketAddress();
        assertNotNull(targetSocketAddress);

        assertEquals("192.168.0.255", targetSocketAddress.getAddress().getHostAddress());
        assertEquals(WOL.DEFAULT_PORT, targetSocketAddress.getPort());
    }

    @Test
    void withInetAddress() throws UnknownHostException {
        WOL wol = wolBuilder.withInetAddress("192.168.3.1").build();

        assertEquals(macMock, wol.getMac());

        InetSocketAddress targetSocketAddress = wol.getTargetSocketAddress();
        assertNotNull(targetSocketAddress);

        assertEquals("192.168.3.1", targetSocketAddress.getAddress().getHostAddress());
        assertEquals(WOL.DEFAULT_PORT, targetSocketAddress.getPort());
    }

    @Test
    void testWithInetAddress() throws UnknownHostException {
        WOL wol = wolBuilder.withInetAddress(InetAddress.getByName("192.168.3.1")).build();

        assertEquals(macMock, wol.getMac());

        InetSocketAddress targetSocketAddress = wol.getTargetSocketAddress();
        assertNotNull(targetSocketAddress);

        assertEquals("192.168.3.1", targetSocketAddress.getAddress().getHostAddress());
        assertEquals(WOL.DEFAULT_PORT, targetSocketAddress.getPort());
    }


    @Test
    void withPort() {
        WOL wol = wolBuilder.withPort(10).build();

        assertEquals(macMock, wol.getMac());

        InetSocketAddress targetSocketAddress = wol.getTargetSocketAddress();
        assertNotNull(targetSocketAddress);

        assertEquals(10, targetSocketAddress.getPort());
        assertEquals("192.168.0.255", targetSocketAddress.getAddress().getHostAddress());
    }

}