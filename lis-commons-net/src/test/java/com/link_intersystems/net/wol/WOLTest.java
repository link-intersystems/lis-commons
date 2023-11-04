package com.link_intersystems.net.wol;

import com.link_intersystems.net.MAC;
import com.link_intersystems.net.MACFormat;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class WOLTest {

    @Test
    void send() throws IOException, ParseException {
        WOL wol = new WOLBuilder(new MACFormat(':').parse("00:B0:D0:63:C2:26"))
                .withInetAddress("192.168.178.1")
                .withDefaultPort()
                .build();

        assertEquals(InetAddress.getByName("192.168.178.1"), wol.getTargetSocketAddress().getAddress());
        assertEquals(WOL.DEFAULT_PORT, wol.getTargetSocketAddress().getPort());
        assertEquals(new MAC(new byte[]{0x00, (byte) 0xB0, (byte) 0xD0, 0x63, (byte) 0xC2, 0x26}), wol.getMac());

        DatagramSocket datagramSocket = mock(DatagramSocket.class);
        wol.send(datagramSocket);

        ArgumentCaptor<DatagramPacket> argumentCaptor = ArgumentCaptor.forClass(DatagramPacket.class);

        verify(datagramSocket, times(1)).send(argumentCaptor.capture());

        DatagramPacket datagramPacket = argumentCaptor.getValue();

        assertEquals(InetAddress.getByName("192.168.178.1"), datagramPacket.getAddress());
        assertEquals(9, datagramPacket.getPort());

        assertArrayEquals(bytes(
                0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, //
                0x00, 0xB0, 0xD0, 0x63, 0xC2, 0x26, //
                0x00, 0xB0, 0xD0, 0x63, 0xC2, 0x26, //
                0x00, 0xB0, 0xD0, 0x63, 0xC2, 0x26, //
                0x00, 0xB0, 0xD0, 0x63, 0xC2, 0x26, //
                0x00, 0xB0, 0xD0, 0x63, 0xC2, 0x26, //
                0x00, 0xB0, 0xD0, 0x63, 0xC2, 0x26, //
                0x00, 0xB0, 0xD0, 0x63, 0xC2, 0x26, //
                0x00, 0xB0, 0xD0, 0x63, 0xC2, 0x26, //
                0x00, 0xB0, 0xD0, 0x63, 0xC2, 0x26, //
                0x00, 0xB0, 0xD0, 0x63, 0xC2, 0x26, //
                0x00, 0xB0, 0xD0, 0x63, 0xC2, 0x26, //
                0x00, 0xB0, 0xD0, 0x63, 0xC2, 0x26, //
                0x00, 0xB0, 0xD0, 0x63, 0xC2, 0x26, //
                0x00, 0xB0, 0xD0, 0x63, 0xC2, 0x26, //
                0x00, 0xB0, 0xD0, 0x63, 0xC2, 0x26, //
                0x00, 0xB0, 0xD0, 0x63, 0xC2, 0x26 //
        ), datagramPacket.getData());
    }

    private byte[] bytes(int... bytes) {
        byte[] asBytes = new byte[bytes.length];

        for (int i = 0; i < bytes.length; i++) {
            asBytes[i] = (byte) bytes[i];
        }

        return asBytes;
    }

}