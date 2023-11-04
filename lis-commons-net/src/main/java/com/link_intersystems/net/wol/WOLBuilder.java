package com.link_intersystems.net.wol;

import com.link_intersystems.net.MAC;
import com.link_intersystems.net.MACFormat;

import java.net.*;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class WOLBuilder {

    private MAC mac;
    private InetAddress inetAddress;
    private int port = WOL.DEFAULT_PORT;

    public WOLBuilder withMac(byte[] mac) {
        this.mac = new MAC(mac);
        return this;
    }

    public WOLBuilder withMac(String mac) throws ParseException {
        return withMac(mac, MACFormat.IEEE_802_OCTET_DELIM);
    }

    public WOLBuilder withMac(String mac, char octetDelim) throws ParseException {
        MACFormat macFormat = new MACFormat();
        macFormat.setOctetDelim(octetDelim);
        return withMac(macFormat.parse(mac));
    }

    public WOLBuilder withMac(MAC mac) {
        this.mac = Objects.requireNonNull(mac);
        return this;
    }

    public WOLBuilder withDefaultBroadcastAddress() throws SocketException {
        return withBroadcastAddress(nic -> {
            Enumeration<InetAddress> inetAddresses = nic.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress inetAddress = inetAddresses.nextElement();
                if (inetAddress instanceof Inet4Address) {
                    Inet4Address inet4Address = (Inet4Address) inetAddress;
                    if (!inet4Address.isLoopbackAddress()) {
                        return true;
                    }
                }
            }
            return false;
        });
    }

    public WOLBuilder withBroadcastAddress(Predicate<NetworkInterface> networkInterfaceSelector) throws SocketException {
        Optional<NetworkInterface> networkInterface = NetworkInterface.networkInterfaces().filter(networkInterfaceSelector).findFirst();

        return networkInterface.map(this::withBroadcastAddress).orElseThrow(() -> new IllegalStateException("Unable to select a network interface"));
    }

    public WOLBuilder withBroadcastAddress(NetworkInterface networkInterface) {
        List<InterfaceAddress> interfaceAddresses = networkInterface.getInterfaceAddresses();
        if (interfaceAddresses.isEmpty()) {
            throw new IllegalArgumentException(networkInterface + " doesn't have any interface addresses.");
        }

        InetAddress broadcast = interfaceAddresses.get(0).getBroadcast();
        return withInetAddress(broadcast);
    }

    public WOLBuilder withInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
        return this;
    }

    public WOLBuilder withInetAddress(String inetAddress) throws UnknownHostException {
        this.inetAddress = InetAddress.getByName(inetAddress);
        return this;
    }

    public WOLBuilder withDefaultPort() {
        return withPort(WOL.DEFAULT_PORT);
    }

    public WOLBuilder withPort(int port) {
        this.port = port;
        return this;
    }

    public WOL build() {
        return new WOL(mac, new InetSocketAddress(inetAddress, port));
    }
}
