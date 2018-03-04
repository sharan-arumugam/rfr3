package com.lti.rfr.service.impl;

import com.lti.rfr.service.EmailAddressService;
import com.lti.rfr.domain.EmailAddress;
import com.lti.rfr.repository.EmailAddressRepository;
import com.lti.rfr.service.dto.EmailAddressDTO;
import com.lti.rfr.service.mapper.EmailAddressMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing EmailAddress.
 */
@Service
@Transactional
public class EmailAddressServiceImpl implements EmailAddressService {

    private final Logger log = LoggerFactory.getLogger(EmailAddressServiceImpl.class);

    private final EmailAddressRepository emailAddressRepository;

    private final EmailAddressMapper emailAddressMapper;

    public EmailAddressServiceImpl(EmailAddressRepository emailAddressRepository, EmailAddressMapper emailAddressMapper) {
        this.emailAddressRepository = emailAddressRepository;
        this.emailAddressMapper = emailAddressMapper;
    }

    /**
     * Save a emailAddress.
     *
     * @param emailAddressDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public EmailAddressDTO save(EmailAddressDTO emailAddressDTO) {
        log.debug("Request to save EmailAddress : {}", emailAddressDTO);
        EmailAddress emailAddress = emailAddressMapper.toEntity(emailAddressDTO);
        emailAddress = emailAddressRepository.save(emailAddress);
        return emailAddressMapper.toDto(emailAddress);
    }

    /**
     * Get all the emailAddresses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EmailAddressDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EmailAddresses");
        return emailAddressRepository.findAll(pageable)
            .map(emailAddressMapper::toDto);
    }

    /**
     * Get one emailAddress by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public EmailAddressDTO findOne(Long id) {
        log.debug("Request to get EmailAddress : {}", id);
        EmailAddress emailAddress = emailAddressRepository.findOne(id);
        return emailAddressMapper.toDto(emailAddress);
    }

    /**
     * Delete the emailAddress by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete EmailAddress : {}", id);
        emailAddressRepository.delete(id);
    }
}
