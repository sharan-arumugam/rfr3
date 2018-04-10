package com.lti.rfr.service.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
// @EqualsAndHashCode(callSuper = false, of = { "name" })
public class Imt2DTO {

    private String id;
    private String name;
    private List<String> children;
    private boolean selected;

    private String imt2;
    private String imt1;
    private String imt;

    public Imt2DTO(long count, String name, String imt1, String imt) {
        this.id = "IMT-2:" + count;
        this.name = name;

        this.imt2 = name;
        this.imt1 = imt1;
        this.imt = imt;
    }

}
