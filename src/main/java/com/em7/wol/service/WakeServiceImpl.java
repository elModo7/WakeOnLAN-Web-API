package com.em7.wol.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static com.em7.wol.service.WakeStrategy.BROADCAST_TO_IP;

@Slf4j
@Service
@RequiredArgsConstructor
public class WakeServiceImpl implements WakeService {

    private final WakeStrategy wakeStrategy = BROADCAST_TO_IP;

    @Override
    public void sendMagicPacket(String ipStr, String macStr) {
        try {
            final byte[] macBytes = getMacBytes(macStr);
            final byte[] bytes = new byte[6 + 16 * macBytes.length];
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) 0xff;
            }
            for (int i = 6; i < bytes.length; i += macBytes.length) {
                System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
            }

            final InetAddress address = InetAddress.getByName(this.wakeStrategy.processIp(ipStr));
            final DatagramPacket packet7 = new DatagramPacket(bytes, bytes.length, address, 7);
            final DatagramPacket packet9 = new DatagramPacket(bytes, bytes.length, address, 9);
            try (DatagramSocket socket = new DatagramSocket()) {
                socket.setBroadcast(this.wakeStrategy.isBroadcast());
                // Send to two main WoL ports
                socket.send(packet7);
                socket.send(packet9);
            }

            log.info("Wake-on-LAN packet sent to ip: " + ipStr + " mac: " + macStr);
        } catch (Exception e) {
            log.error("Failed to send Wake-on-LAN packet to ip: " + ipStr + " mac: " + macStr + " -> ", e);
        }
    }

    private static byte[] getMacBytes(String macStr) throws IllegalArgumentException {
        final byte[] bytes = new byte[6];
        final String[] hex = macStr.split("(\\:|\\-)");
        if (hex.length != 6) {
            throw new IllegalArgumentException("Invalid MAC address.");
        }
        try {
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) Integer.parseInt(hex[i], 16);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex digit in MAC address.");
        }
        return bytes;
    }
}
