package com.lti.rfr.service;

import com.lti.rfr.service.dto.RecieverDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Reciever.
 */
public interface RecieverService {

    /**
     * Save a reciever.
     *
     * @param recieverDTO the entity to save
     * @return the persisted entity
     */
    RecieverDTO save(RecieverDTO recieverDTO);

    /**
     * Get all the recievers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<RecieverDTO> findAll(Pageable pageable);

    /**
     * Get the "id" reciever.
     *
     * @param id the id of the entity
     * @return the entity
     */
    RecieverDTO findOne(Long id);

    /**
     * Delete the "id" reciever.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
