package com.lti.rfr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lti.rfr.domain.FunctionalGroup;

/**
 * Spring Data JPA repository for the FunctionalGroup entity.
 */
@Repository
public interface FunctionalGroupRepository extends JpaRepository<FunctionalGroup, Long> {

    @Query("select distinct g.imt from FunctionalGroup g group by g.id")
    List<String> findAllImt();

    @Query("select distinct g.imt1 from FunctionalGroup g group by g.id")
    List<String> findAllImt1();

    @Query("select distinct g.imt2 from FunctionalGroup g group by g.id")
    List<String> findAllImt2();

}
