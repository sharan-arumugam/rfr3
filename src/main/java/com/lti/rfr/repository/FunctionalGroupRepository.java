package com.lti.rfr.repository;

import com.lti.rfr.domain.FunctionalGroup;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the FunctionalGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FunctionalGroupRepository extends JpaRepository<FunctionalGroup, Long> {

}
