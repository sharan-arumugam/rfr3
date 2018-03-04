package com.lti.rfr.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the EmailAddress entity.
 */
public class EmailAddressDTO implements Serializable {

    private Long id;

    private String psNumber;

    private String name;

    private String appleMail;

    private Long psNumberId;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppleMail() {
        return appleMail;
    }

    public void setAppleMail(String appleMail) {
        this.appleMail = appleMail;
    }

    public Long getPsNumberId() {
        return psNumberId;
    }

    public void setPsNumberId(Long functionalGroupId) {
        this.psNumberId = functionalGroupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EmailAddressDTO emailAddressDTO = (EmailAddressDTO) o;
        if(emailAddressDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), emailAddressDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EmailAddressDTO{" +
            "id=" + getId() +
            ", psNumber='" + getPsNumber() + "'" +
            ", name='" + getName() + "'" +
            ", appleMail='" + getAppleMail() + "'" +
            "}";
    }
}
