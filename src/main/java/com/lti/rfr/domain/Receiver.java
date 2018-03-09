package com.lti.rfr.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.lti.rfr.service.dto.ReceiverDTO;

/**
 * A Receiver.
 */
@Entity
public class Receiver implements Serializable {

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

    private String groups;
    private String selectedImts;
    private String selectedImt1s;
    private String selectedImt2s;

    public Receiver() {
    }

    public Receiver(ReceiverDTO recieverDTO) {
        id = recieverDTO.getId();
        psNumber = recieverDTO.getPsNumber();
        appleMail = recieverDTO.getAppleMail();
        name = recieverDTO.getName();
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not
    // remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPsNumber() {
        return psNumber;
    }

    public Receiver psNumber(String psNumber) {
        this.psNumber = psNumber;
        return this;
    }

    public void setPsNumber(String psNumber) {
        this.psNumber = psNumber;
    }

    public String getAppleMail() {
        return appleMail;
    }

    public Receiver appleMail(String appleMail) {
        this.appleMail = appleMail;
        return this;
    }

    public void setAppleMail(String appleMail) {
        this.appleMail = appleMail;
    }

    public String getName() {
        return name;
    }

    public Receiver name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Receiver reciever = (Receiver) o;
        if (reciever.getPsNumber() == null || getPsNumber() == null) {
            return false;
        }
        return Objects.equals(getPsNumber(), reciever.getPsNumber());
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public String getSelectedImts() {
        return selectedImts;
    }

    public void setSelectedImts(String selectedImts) {
        this.selectedImts = selectedImts;
    }

    public String getSelectedImt1s() {
        return selectedImt1s;
    }

    public void setSelectedImt1s(String selectedImt1s) {
        this.selectedImt1s = selectedImt1s;
    }

    public String getSelectedImt2s() {
        return selectedImt2s;
    }

    public void setSelectedImt2s(String selectedImt2s) {
        this.selectedImt2s = selectedImt2s;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getPsNumber());
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
