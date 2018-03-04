package com.lti.rfr.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Reciever entity.
 */
public class RecieverDTO implements Serializable {

    private Long id;

    private String psNumber;

    private String appleMail;

    private String name;

    private Long groupId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPsNumber() {
        return psNumber;
    }

    public void setPsNumber(String psNumber) {
        this.psNumber = psNumber;
    }

    public String getAppleMail() {
        return appleMail;
    }

    public void setAppleMail(String appleMail) {
        this.appleMail = appleMail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long functionalGroupId) {
        this.groupId = functionalGroupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RecieverDTO recieverDTO = (RecieverDTO) o;
        if(recieverDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), recieverDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RecieverDTO{" +
            "id=" + getId() +
            ", psNumber='" + getPsNumber() + "'" +
            ", appleMail='" + getAppleMail() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
