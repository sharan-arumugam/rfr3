package com.lti.rfr.service.dto;

import java.util.List;

import lombok.Data;

@Data
//@EqualsAndHashCode(callSuper = false, of = { "name" })
public class Imt2DTO {

    private String id;
    private String name;
    private List<String> children;
    private boolean selected;

}
