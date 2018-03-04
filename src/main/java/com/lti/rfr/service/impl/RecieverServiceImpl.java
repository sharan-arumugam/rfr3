package com.lti.rfr.service.impl;

import com.lti.rfr.service.RecieverService;
import com.lti.rfr.domain.Reciever;
import com.lti.rfr.repository.RecieverRepository;
import com.lti.rfr.service.dto.RecieverDTO;
import com.lti.rfr.service.mapper.RecieverMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Reciever.
 */
@Service
@Transactional
public class RecieverServiceImpl implements RecieverService {

    private final Logger log = LoggerFactory.getLogger(RecieverServiceImpl.class);

    private final RecieverRepository recieverRepository;

    private final RecieverMapper recieverMapper;

    public RecieverServiceImpl(RecieverRepository recieverRepository, RecieverMapper recieverMapper) {
        this.recieverRepository = recieverRepository;
        this.recieverMapper = recieverMapper;
    }

    /**
     * Save a reciever.
     *
     * @param recieverDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public RecieverDTO save(RecieverDTO recieverDTO) {
        log.debug("Request to save Reciever : {}", recieverDTO);
        Reciever reciever = recieverMapper.toEntity(recieverDTO);
        reciever = recieverRepository.save(reciever);
        return recieverMapper.toDto(reciever);
    }

    /**
     * Get all the recievers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RecieverDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Recievers");
        return recieverRepository.findAll(pageable)
            .map(recieverMapper::toDto);
    }

    /**
     * Get one reciever by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public RecieverDTO findOne(Long id) {
        log.debug("Request to get Reciever : {}", id);
        Reciever reciever = recieverRepository.findOne(id);
        return recieverMapper.toDto(reciever);
    }

    /**
     * Delete the reciever by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Reciever : {}", id);
        recieverRepository.delete(id);
    }
}
