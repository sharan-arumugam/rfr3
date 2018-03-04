package com.lti.rfr.service;

import com.lti.rfr.service.dto.EmailAddressDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing EmailAddress.
 */
public interface EmailAddressService {

    /**
     * Save a emailAddress.
     *
     * @param emailAddressDTO the entity to save
     * @return the persisted entity
     */
    EmailAddressDTO save(EmailAddressDTO emailAddressDTO);

    /**
     * Get all the emailAddresses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<EmailAddressDTO> findAll(Pageable pageable);

    /**
     * Get the "id" emailAddress.
     *
     * @param id the id of the entity
     * @return the entity
     */
    EmailAddressDTO findOne(Long id);

    /**
     * Delete the "id" emailAddress.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
