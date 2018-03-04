package com.lti.rfr.repository;

import com.lti.rfr.domain.Reciever;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Reciever entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecieverRepository extends JpaRepository<Reciever, Long> {

}
