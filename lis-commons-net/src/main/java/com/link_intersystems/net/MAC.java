package com.link_intersystems.net;

import java.util.Arrays;

public class MAC {

    private static final int HARDWARE_ADDRESS_LENGTH = 6;

    private final byte[] hardwareAddress;

    public MAC(byte[] hardwareAddress) {
        if (hardwareAddress.length != HARDWARE_ADDRESS_LENGTH) {
            throw new IllegalArgumentException("Not a valid mac address");
        }

        this.hardwareAddress = hardwareAddress;
    }

    @Override
    public String toString() {

        return toString(MACFormat.IEEE_802_OCTET_DELIM);
    }

    public String toString(char octetDelim) {
        MACFormat macFormat = new MACFormat();
        macFormat.setOctetDelim(octetDelim);
        return macFormat.format(this);
    }

    public byte[] toByteArray() {
        return hardwareAddress.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MAC mac = (MAC) o;
        return Arrays.equals(hardwareAddress, mac.hardwareAddress);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(hardwareAddress);
    }
}
