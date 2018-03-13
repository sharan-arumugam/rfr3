package com.lti.rfr.service;

import com.lti.rfr.service.dto.ReceiverDTO;
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
    ReceiverDTO save(ReceiverDTO recieverDTO);

    /**
     * Get all the recievers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ReceiverDTO> findAll(Pageable pageable);

    /**
     * Get the "id" reciever.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ReceiverDTO findOne(Long id);

    /**
     * Delete the "id" reciever.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    void mail(ReceiverDTO recieverDTO);
}
