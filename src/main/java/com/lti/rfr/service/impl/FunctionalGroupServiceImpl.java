package com.lti.rfr.service.impl;

import com.lti.rfr.service.FunctionalGroupService;
import com.lti.rfr.domain.FunctionalGroup;
import com.lti.rfr.repository.FunctionalGroupRepository;
import com.lti.rfr.service.dto.FunctionalGroupDTO;
import com.lti.rfr.service.mapper.FunctionalGroupMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing FunctionalGroup.
 */
@Service
@Transactional
public class FunctionalGroupServiceImpl implements FunctionalGroupService {

    private final Logger log = LoggerFactory.getLogger(FunctionalGroupServiceImpl.class);

    private final FunctionalGroupRepository functionalGroupRepository;

    private final FunctionalGroupMapper functionalGroupMapper;

    public FunctionalGroupServiceImpl(FunctionalGroupRepository functionalGroupRepository, FunctionalGroupMapper functionalGroupMapper) {
        this.functionalGroupRepository = functionalGroupRepository;
        this.functionalGroupMapper = functionalGroupMapper;
    }

    /**
     * Save a functionalGroup.
     *
     * @param functionalGroupDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FunctionalGroupDTO save(FunctionalGroupDTO functionalGroupDTO) {
        log.debug("Request to save FunctionalGroup : {}", functionalGroupDTO);
        FunctionalGroup functionalGroup = functionalGroupMapper.toEntity(functionalGroupDTO);
        functionalGroup = functionalGroupRepository.save(functionalGroup);
        return functionalGroupMapper.toDto(functionalGroup);
    }

    /**
     * Get all the functionalGroups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FunctionalGroupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FunctionalGroups");
        return functionalGroupRepository.findAll(pageable)
            .map(functionalGroupMapper::toDto);
    }

    /**
     * Get one functionalGroup by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public FunctionalGroupDTO findOne(Long id) {
        log.debug("Request to get FunctionalGroup : {}", id);
        FunctionalGroup functionalGroup = functionalGroupRepository.findOne(id);
        return functionalGroupMapper.toDto(functionalGroup);
    }

    /**
     * Delete the functionalGroup by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FunctionalGroup : {}", id);
        functionalGroupRepository.delete(id);
    }
}
