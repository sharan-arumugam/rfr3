package com.lti.rfr.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A FunctionalGroup.
 */
@Entity
@Table(name = "functional_group")
public class FunctionalGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "imt")
    private String imt;

    @Column(name = "imt_1")
    private String imt1;

    @Column(name = "imt_2")
    private String imt2;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImt() {
        return imt;
    }

    public FunctionalGroup imt(String imt) {
        this.imt = imt;
        return this;
    }

    public void setImt(String imt) {
        this.imt = imt;
    }

    public String getImt1() {
        return imt1;
    }

    public FunctionalGroup imt1(String imt1) {
        this.imt1 = imt1;
        return this;
    }

    public void setImt1(String imt1) {
        this.imt1 = imt1;
    }

    public String getImt2() {
        return imt2;
    }

    public FunctionalGroup imt2(String imt2) {
        this.imt2 = imt2;
        return this;
    }

    public void setImt2(String imt2) {
        this.imt2 = imt2;
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
        FunctionalGroup functionalGroup = (FunctionalGroup) o;
        if (functionalGroup.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), functionalGroup.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FunctionalGroup{" +
            "id=" + getId() +
            ", imt='" + getImt() + "'" +
            ", imt1='" + getImt1() + "'" +
            ", imt2='" + getImt2() + "'" +
            "}";
    }
}
