package com.em7.wol.dto.in;

import lombok.Data;

@Data
public class InDeviceDTO {
    private Integer id;
    private String name;
    private String description;
    private String ip;
    private String mac;
    private boolean status;
}
