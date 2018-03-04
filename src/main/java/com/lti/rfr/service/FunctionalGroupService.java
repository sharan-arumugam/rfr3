package com.lti.rfr.service;

import com.lti.rfr.service.dto.FunctionalGroupDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing FunctionalGroup.
 */
public interface FunctionalGroupService {

    /**
     * Save a functionalGroup.
     *
     * @param functionalGroupDTO the entity to save
     * @return the persisted entity
     */
    FunctionalGroupDTO save(FunctionalGroupDTO functionalGroupDTO);

    /**
     * Get all the functionalGroups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FunctionalGroupDTO> findAll(Pageable pageable);

    /**
     * Get the "id" functionalGroup.
     *
     * @param id the id of the entity
     * @return the entity
     */
    FunctionalGroupDTO findOne(Long id);

    /**
     * Delete the "id" functionalGroup.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
