package com.lti.rfr.web.rest;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.lti.rfr.repository.FunctionalGroupRepository;
import com.lti.rfr.service.FunctionalGroupService;
import com.lti.rfr.service.dto.FunctionalGroupDTO;
import com.lti.rfr.service.dto.Imt1DTO;
import com.lti.rfr.service.dto.Imt2DTO;
import com.lti.rfr.service.dto.ImtDTO;
import com.lti.rfr.service.dto.ReceiverDTO;
import com.lti.rfr.web.rest.errors.BadRequestAlertException;
import com.lti.rfr.web.rest.util.HeaderUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing FunctionalGroup.
 */
@RestController
@RequestMapping("/api")
public class FunctionalGroupResource {

    private final Logger log = LoggerFactory.getLogger(FunctionalGroupResource.class);

    private static final String ENTITY_NAME = "functionalGroup";

    private final FunctionalGroupService functionalGroupService;
    private final FunctionalGroupRepository functionalGroupRepository;

    public FunctionalGroupResource(FunctionalGroupService functionalGroupService,
            FunctionalGroupRepository functionalGroupRepository) {

        this.functionalGroupRepository = functionalGroupRepository;
        this.functionalGroupService = functionalGroupService;
    }

    /**
     * POST /functional-groups : Create a new functionalGroup.
     *
     * @param functionalGroupDTO
     *            the functionalGroupDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new
     *         functionalGroupDTO, or with status 400 (Bad Request) if the
     *         functionalGroup has already an ID
     * @throws URISyntaxException
     *             if the Location URI syntax is incorrect
     */
    @PostMapping("/functional-groups")
    @Timed
    public ResponseEntity<FunctionalGroupDTO> createFunctionalGroup(@RequestBody FunctionalGroupDTO functionalGroupDTO)
            throws URISyntaxException {
        log.debug("REST request to save FunctionalGroup : {}", functionalGroupDTO);
        if (functionalGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new functionalGroup cannot already have an ID", ENTITY_NAME,
                    "idexists");
        }
        FunctionalGroupDTO result = functionalGroupService.save(functionalGroupDTO);
        return ResponseEntity.created(new URI("/api/functional-groups/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * PUT /functional-groups : Updates an existing functionalGroup.
     *
     * @param functionalGroupDTO
     *            the functionalGroupDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     *         functionalGroupDTO, or with status 400 (Bad Request) if the
     *         functionalGroupDTO is not valid, or with status 500 (Internal Server
     *         Error) if the functionalGroupDTO couldn't be updated
     * @throws URISyntaxException
     *             if the Location URI syntax is incorrect
     */
    @PutMapping("/functional-groups")
    @Timed
    public ResponseEntity<FunctionalGroupDTO> updateFunctionalGroup(@RequestBody FunctionalGroupDTO functionalGroupDTO)
            throws URISyntaxException {
        log.debug("REST request to update FunctionalGroup : {}", functionalGroupDTO);
        if (functionalGroupDTO.getId() == null) {
            return createFunctionalGroup(functionalGroupDTO);
        }
        FunctionalGroupDTO result = functionalGroupService.save(functionalGroupDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, functionalGroupDTO.getId().toString()))
                .body(result);
    }

    /**
     * GET /functional-groups : get all the functionalGroups.
     *
     * @param pageable
     *            the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of
     *         functionalGroups in body
     */
    @GetMapping("/functional-groups")
    @Timed
    public List<ImtDTO> getAllFunctionalGroups(Pageable pageable) {

        List<FunctionalGroupDTO> list = functionalGroupRepository
                .findAll()
                .stream()
                .map(FunctionalGroupDTO::new)
                .collect(toList());

        Map<String, List<FunctionalGroupDTO>> imtMap = list.stream()
                .filter(dto -> null != dto.getImt())
                .collect(groupingBy(FunctionalGroupDTO::getImt));

        List<ImtDTO> imtList = new ArrayList<>();

        AtomicLong imtCounter = new AtomicLong(100);
        AtomicLong imt1Counter = new AtomicLong(2000);
        AtomicLong imt2Counter = new AtomicLong(30000);

        imtMap.forEach((imt, imtDtoList) -> {

            ImtDTO imtDTO = new ImtDTO(imtCounter.addAndGet(1), imt);
            List<Imt1DTO> imt1List = new ArrayList<>();

            imtDtoList
                    .stream()
                    .filter(dto -> null != dto.getImt1())
                    .collect(groupingBy(FunctionalGroupDTO::getImt1))
                    .forEach((imt1, dtoList) -> {

                        Imt1DTO imt1DTO = new Imt1DTO(imt1Counter.addAndGet(1), imt1, imt);
                        List<Imt2DTO> imt2List = new ArrayList<>();

                        dtoList.stream()
                                .filter(dto -> null != dto.getImt2())
                                .collect(groupingBy(FunctionalGroupDTO::getImt2))
                                .forEach((imt2, imt2Groups) -> {

                                    for (FunctionalGroupDTO im : imt2Groups) {
                                        Imt2DTO imt2DTO = new Imt2DTO(imt2Counter.addAndGet(1), imt2, imt1, imt);
                                        imt2DTO.setChildren(new ArrayList<>());
                                        imt2List.add(imt2DTO);
                                    }
                                });
                        imt1DTO.setChildren(imt2List);
                        imt1List.add(imt1DTO);
                    });
            imtDTO.setChildren(imt1List);
            imtList.add(imtDTO);
        });

        return imtList;
    }

    /**
     * GET /functional-groups/:id : get the "id" functionalGroup.
     *
     * @param id
     *            the id of the functionalGroupDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the
     *         functionalGroupDTO, or with status 404 (Not Found)
     */
    @GetMapping("/functional-groups/{id}")
    @Timed
    public ResponseEntity<FunctionalGroupDTO> getFunctionalGroup(@PathVariable Long id) {
        log.debug("REST request to get FunctionalGroup : {}", id);
        FunctionalGroupDTO functionalGroupDTO = functionalGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(functionalGroupDTO));
    }

    /**
     * DELETE /functional-groups/:id : delete the "id" functionalGroup.
     *
     * @param id
     *            the id of the functionalGroupDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/functional-groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteFunctionalGroup(@PathVariable Long id) {
        log.debug("REST request to delete FunctionalGroup : {}", id);
        functionalGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    public List<ImtDTO> getAllFunctionalGroupMaster(ReceiverDTO receiverDTO) throws JsonProcessingException {

        List<ImtDTO> masterList = getAllFunctionalGroups(null);

        Predicate<String> isImtSelected = receiverDTO.getSelectedImts()::contains;
        Predicate<String> isImt1Selected = receiverDTO.getSelectedImt1s()::contains;
        Predicate<String> isImt2Selected = receiverDTO.getSelectedImt2s()::contains;

        masterList
                .stream()
                .forEach(imtDto -> {

                    imtDto.setSelected(isImtSelected.test(imtDto.getName()));
                    imtDto.setChildren(null == imtDto.getChildren()
                            ? new ArrayList<>()
                            : imtDto.getChildren());

                    imtDto.getChildren()
                            .stream()
                            .forEach(imt1Dto -> {

                                imt1Dto.setSelected(isImt1Selected.test(imt1Dto.getName()));
                                imt1Dto.setChildren(null == imt1Dto.getChildren()
                                        ? new ArrayList<>()
                                        : imt1Dto.getChildren());

                                imt1Dto.getChildren()
                                        .stream()
                                        .forEach(imt2Dto -> {
                                            imt2Dto.setSelected(isImt2Selected.test(imt2Dto.getName()));
                                        });
                            });
                });

        return masterList;
    }
}
