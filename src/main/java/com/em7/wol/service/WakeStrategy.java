package com.em7.wol.service;

import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public enum WakeStrategy {
    BROADCAST_TO_ALL(true, ip -> "255.255.255.255"),
    BROADCAST_TO_IP(true, ip -> ip.replaceAll("\\d{1,}$", "255")),
    SEND_TO_IP(false, ip -> ip);

    private final boolean isBroadcast;
    private final Function<String, String> ipProcessor;

    public String processIp(String ip) {
        return this.ipProcessor.apply(ip);
    }

    public boolean isBroadcast() {
        return this.isBroadcast;
    }
}