package com.lti.rfr.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Reciever.
 */
@Entity
@Table(name = "reciever")
public class Reciever implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ps_number")
    private String psNumber;

    @Column(name = "apple_mail")
    private String appleMail;

    @Column(name = "name")
    private String name;

    @ManyToOne
    private FunctionalGroup group;

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

    public Reciever psNumber(String psNumber) {
        this.psNumber = psNumber;
        return this;
    }

    public void setPsNumber(String psNumber) {
        this.psNumber = psNumber;
    }

    public String getAppleMail() {
        return appleMail;
    }

    public Reciever appleMail(String appleMail) {
        this.appleMail = appleMail;
        return this;
    }

    public void setAppleMail(String appleMail) {
        this.appleMail = appleMail;
    }

    public String getName() {
        return name;
    }

    public Reciever name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FunctionalGroup getGroup() {
        return group;
    }

    public Reciever group(FunctionalGroup functionalGroup) {
        this.group = functionalGroup;
        return this;
    }

    public void setGroup(FunctionalGroup functionalGroup) {
        this.group = functionalGroup;
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
        Reciever reciever = (Reciever) o;
        if (reciever.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reciever.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Reciever{" +
            "id=" + getId() +
            ", psNumber='" + getPsNumber() + "'" +
            ", appleMail='" + getAppleMail() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
