package com.lti.rfr.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A EmailAddress.
 */
@Entity
@Table(name = "email_address")
public class EmailAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ps_number")
    private String psNumber;

    @Column(name = "name")
    private String name;

    @Column(name = "apple_mail")
    private String appleMail;

    @ManyToOne
    private FunctionalGroup psNumber;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPsNumber() {
        return psNumber;
    }

    public EmailAddress psNumber(String psNumber) {
        this.psNumber = psNumber;
        return this;
    }

    public void setPsNumber(String psNumber) {
        this.psNumber = psNumber;
    }

    public String getName() {
        return name;
    }

    public EmailAddress name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppleMail() {
        return appleMail;
    }

    public EmailAddress appleMail(String appleMail) {
        this.appleMail = appleMail;
        return this;
    }

    public void setAppleMail(String appleMail) {
        this.appleMail = appleMail;
    }

    public FunctionalGroup getPsNumber() {
        return psNumber;
    }

    public EmailAddress psNumber(FunctionalGroup functionalGroup) {
        this.psNumber = functionalGroup;
        return this;
    }

    public void setPsNumber(FunctionalGroup functionalGroup) {
        this.psNumber = functionalGroup;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EmailAddress emailAddress = (EmailAddress) o;
        if (emailAddress.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), emailAddress.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EmailAddress{" +
            "id=" + getId() +
            ", psNumber='" + getPsNumber() + "'" +
            ", name='" + getName() + "'" +
            ", appleMail='" + getAppleMail() + "'" +
            "}";
    }
}
