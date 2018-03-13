package com.lti.rfr.service.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
// @EqualsAndHashCode(callSuper = false, of = { "name" })
public class Imt1DTO {

    private String id;
    private String name;
    private List<Imt2DTO> children;
    private boolean selected;
    private String imt;
    private String imt1;

    public Imt1DTO(long count, String name, String imt) {
        this.id = "IMT-1:" + count;
        this.name = name;

        this.imt1 = name;
        this.imt = imt;
    }

}
