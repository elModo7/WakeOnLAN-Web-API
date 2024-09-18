package com.em7.wol.dto.out;

import lombok.Data;

@Data
public class OutDeviceDTO {
    private int id;
    private String name;
    private String description;
    private String ip;
    private String mac;
    private boolean status;
    private boolean shutdownable;
}
