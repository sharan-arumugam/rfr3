package com.lti.rfr.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the FunctionalGroup entity.
 */
public class FunctionalGroupDTO implements Serializable {

    private Long id;

    private String imt;

    private String imt1;

    private String imt2;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImt() {
        return imt;
    }

    public void setImt(String imt) {
        this.imt = imt;
    }

    public String getImt1() {
        return imt1;
    }

    public void setImt1(String imt1) {
        this.imt1 = imt1;
    }

    public String getImt2() {
        return imt2;
    }

    public void setImt2(String imt2) {
        this.imt2 = imt2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FunctionalGroupDTO functionalGroupDTO = (FunctionalGroupDTO) o;
        if(functionalGroupDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), functionalGroupDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FunctionalGroupDTO{" +
            "id=" + getId() +
            ", imt='" + getImt() + "'" +
            ", imt1='" + getImt1() + "'" +
            ", imt2='" + getImt2() + "'" +
            "}";
    }
}
