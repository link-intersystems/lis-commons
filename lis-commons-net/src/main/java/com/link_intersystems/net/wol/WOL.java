package com.link_intersystems.net.wol;

import com.link_intersystems.net.MAC;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import static java.util.Objects.requireNonNull;

public class WOL {

    public static final int DEFAULT_PORT = 9;
    private final MAC mac;
    private final InetSocketAddress targetSocketAddress;

    public WOL(MAC mac, InetSocketAddress targetSocketAddress) {
        this.mac = requireNonNull(mac);
        this.targetSocketAddress = requireNonNull(targetSocketAddress);
    }

    public MAC getMac() {
        return mac;
    }

    public InetSocketAddress getTargetSocketAddress() {
        return targetSocketAddress;
    }

    public void send(DatagramSocket datagramSocket) throws IOException {
        DatagramPacket datagramPacket = getDatagramPacket();
        datagramSocket.send(datagramPacket);
    }

    private DatagramPacket getDatagramPacket() {
        MagicPacket magicPacket = new MagicPacket(mac);
        byte[] magicPacketPayload = magicPacket.toByteArray();
        return new DatagramPacket(magicPacketPayload, magicPacketPayload.length, targetSocketAddress);
    }

}
