package com.link_intersystems.net.wol;

import com.link_intersystems.net.MAC;

import java.nio.ByteBuffer;

import static java.util.Objects.requireNonNull;

public class MagicPacket {

    private final MAC mac;
    private byte[] payload;

    public MagicPacket(MAC mac) {
        this.mac = requireNonNull(mac);
    }

    public MAC getMac() {
        return mac;
    }

    public byte[] toByteArray() {
        if (payload == null) {
            this.payload = createPayload();
        }

        return payload.clone();
    }

    private byte[] createPayload() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(102);

        // write synchronization stream
        byte ff = (byte) 0xff;
        byteBuffer.put(new byte[]{ff, ff, ff, ff, ff, ff});

        // repeat mac 16 times
        byte[] macBytes = getMac().toByteArray();
        for (int i = 0; i < 16; i++) {
            byteBuffer.put(macBytes);
        }

        return byteBuffer.array();
    }

}
