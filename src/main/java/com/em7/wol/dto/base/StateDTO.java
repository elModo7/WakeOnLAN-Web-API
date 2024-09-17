package com.em7.wol.dto.base;

import lombok.Data;

@Data
public class StateDTO {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private String name;

}
