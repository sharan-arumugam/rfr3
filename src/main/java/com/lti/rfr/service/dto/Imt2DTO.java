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

    public Imt2DTO(Long id, String imt2) {
        this.id = String.valueOf(id);
        name = imt2;
    }

}
