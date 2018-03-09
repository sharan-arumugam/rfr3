package com.lti.rfr.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lti.rfr.domain.Receiver;
import com.lti.rfr.repository.FunctionalGroupRepository;
import com.lti.rfr.repository.RecieverRepository;
import com.lti.rfr.service.dto.ReceiverDTO;
import com.lti.rfr.service.mapper.RecieverMapper;

/**
 * Service Implementation for managing Reciever.
 */
@Service
@Transactional
public class RecieverServiceImpl implements RecieverService {

    private final Logger log = LoggerFactory.getLogger(RecieverServiceImpl.class);

    private final RecieverRepository recieverRepository;

    private final FunctionalGroupRepository functionalGroupRepository;

    private final RecieverMapper recieverMapper;

    public RecieverServiceImpl(RecieverRepository recieverRepository, RecieverMapper recieverMapper,
            FunctionalGroupRepository functionalGroupRepository) {
        this.recieverRepository = recieverRepository;
        this.recieverMapper = recieverMapper;
        this.functionalGroupRepository = functionalGroupRepository;
    }

    /**
     * Save a reciever.
     *
     * @param recieverDTO
     *            the entity to save
     * @return the persisted entity
     */
    @Override
    public ReceiverDTO save(ReceiverDTO recieverDTO) {
        log.debug("Request to save Reciever : {}", recieverDTO);

        List<String> allImts = functionalGroupRepository.findAllImt();
        List<String> allImt1s = functionalGroupRepository.findAllImt1();
        List<String> allImt2s = functionalGroupRepository.findAllImt2();

        Predicate<String> isImt = allImts::contains;
        Predicate<String> isImt1 = allImt1s::contains;
        Predicate<String> isImt2 = allImt2s::contains;

        List<String> selectedGroups = recieverDTO.getGroups();

        List<String> selectedImts = selectedGroups.stream().filter(isImt).collect(toList());
        List<String> selectedImt1s = selectedGroups.stream().filter(isImt1).collect(toList());
        List<String> selectedImt2s = selectedGroups.stream().filter(isImt2).collect(toList());

        recieverDTO.setGroups(selectedGroups);
        recieverDTO.setSelectedImts(selectedImts);
        recieverDTO.setSelectedImt1s(selectedImt1s);
        recieverDTO.setSelectedImt2s(selectedImt2s);

        Receiver reciever = recieverMapper.toEntity(recieverDTO);
        reciever = recieverRepository.save(reciever);
        return recieverMapper.toDto(reciever);
    }

    /**
     * Get all the recievers.
     *
     * @param pageable
     *            the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReceiverDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Recievers");
        return recieverRepository.findAll(pageable)
                .map(recieverMapper::toDto);
    }

    /**
     * Get one reciever by id.
     *
     * @param id
     *            the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ReceiverDTO findOne(Long id) {
        log.debug("Request to get Reciever : {}", id);
        Receiver reciever = recieverRepository.findOne(id);
        return recieverMapper.toDto(reciever);
    }

    /**
     * Delete the reciever by id.
     *
     * @param id
     *            the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Reciever : {}", id);
        recieverRepository.delete(id);
    }
}
