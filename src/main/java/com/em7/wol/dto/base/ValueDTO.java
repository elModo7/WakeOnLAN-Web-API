package com.em7.wol.dto.base;

import lombok.Data;
import lombok.Value;

import java.util.List;

@Data
public class ValueDTO {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private String value;
    private String parentId;
}
