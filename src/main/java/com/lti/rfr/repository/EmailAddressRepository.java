package com.lti.rfr.repository;

import com.lti.rfr.domain.EmailAddress;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the EmailAddress entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmailAddressRepository extends JpaRepository<EmailAddress, Long> {

}
