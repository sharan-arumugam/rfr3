package com.lti.rfr.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lti.rfr.domain.Rfr;

public interface RfrRepository extends JpaRepository<Rfr, Long> {

    List<Rfr> findByImtIn(List<String> imts);
    
    List<Rfr> findByImt1In(List<String> imt1s);
    
    List<Rfr> findByImt2In(List<String> imt2s);

    List<Rfr> findByRequestIdIn(Set<Long> selectedRfrIds);

}
