package com.em7.wol.service;

public interface WakeService {
    void sendMagicPacket(String ip, String mac);
}