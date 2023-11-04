package com.link_intersystems.net.wol;

import com.link_intersystems.net.MAC;

import java.net.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

public class WOLBuilder {

    private MAC mac;
    private InetAddress inetAddress;
    private int port;

    public WOLBuilder(MAC mac) {
        this.mac = requireNonNull(mac);
        withDefaultPort();
    }

    public WOLBuilder withDefaultBroadcastAddress() {
        return withBroadcastAddress(nic -> {
            try {
                if (nic.isLoopback()) {
                    return false;
                }

                List<InterfaceAddress> interfaceAddresses = nic.getInterfaceAddresses();
                for (InterfaceAddress interfaceAddress : interfaceAddresses) {
                    InetAddress inetAddress = interfaceAddress.getAddress();
                    if (inetAddress instanceof Inet4Address) {
                        return true;
                    }
                }
                return false;
            } catch (SocketException e) {
                throw new RuntimeException(e);
            }

        });
    }

    public WOLBuilder withBroadcastAddress(Predicate<NetworkInterface> networkInterfaceSelector) {
        try {
            Optional<NetworkInterface> networkInterface = NetworkInterface.networkInterfaces().filter(networkInterfaceSelector).findFirst();
            return networkInterface.map(this::withBroadcastAddress).orElseThrow(() -> new IllegalStateException("Unable to select a network interface"));
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
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
        if (inetAddress == null) {
            withDefaultBroadcastAddress();
        }

        return new WOL(mac, new InetSocketAddress(inetAddress, port));
    }
}
