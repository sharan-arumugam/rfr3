package com.lti.rfr.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lti.rfr.service.FunctionalGroupService;
import com.lti.rfr.web.rest.errors.BadRequestAlertException;
import com.lti.rfr.web.rest.util.HeaderUtil;
import com.lti.rfr.web.rest.util.PaginationUtil;
import com.lti.rfr.service.dto.FunctionalGroupDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing FunctionalGroup.
 */
@RestController
@RequestMapping("/api")
public class FunctionalGroupResource {

    private final Logger log = LoggerFactory.getLogger(FunctionalGroupResource.class);

    private static final String ENTITY_NAME = "functionalGroup";

    private final FunctionalGroupService functionalGroupService;

    public FunctionalGroupResource(FunctionalGroupService functionalGroupService) {
        this.functionalGroupService = functionalGroupService;
    }

    /**
     * POST  /functional-groups : Create a new functionalGroup.
     *
     * @param functionalGroupDTO the functionalGroupDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new functionalGroupDTO, or with status 400 (Bad Request) if the functionalGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/functional-groups")
    @Timed
    public ResponseEntity<FunctionalGroupDTO> createFunctionalGroup(@RequestBody FunctionalGroupDTO functionalGroupDTO) throws URISyntaxException {
        log.debug("REST request to save FunctionalGroup : {}", functionalGroupDTO);
        if (functionalGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new functionalGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FunctionalGroupDTO result = functionalGroupService.save(functionalGroupDTO);
        return ResponseEntity.created(new URI("/api/functional-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /functional-groups : Updates an existing functionalGroup.
     *
     * @param functionalGroupDTO the functionalGroupDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated functionalGroupDTO,
     * or with status 400 (Bad Request) if the functionalGroupDTO is not valid,
     * or with status 500 (Internal Server Error) if the functionalGroupDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/functional-groups")
    @Timed
    public ResponseEntity<FunctionalGroupDTO> updateFunctionalGroup(@RequestBody FunctionalGroupDTO functionalGroupDTO) throws URISyntaxException {
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
     * GET  /functional-groups : get all the functionalGroups.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of functionalGroups in body
     */
    @GetMapping("/functional-groups")
    @Timed
    public ResponseEntity<List<FunctionalGroupDTO>> getAllFunctionalGroups(Pageable pageable) {
        log.debug("REST request to get a page of FunctionalGroups");
        Page<FunctionalGroupDTO> page = functionalGroupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/functional-groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /functional-groups/:id : get the "id" functionalGroup.
     *
     * @param id the id of the functionalGroupDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the functionalGroupDTO, or with status 404 (Not Found)
     */
    @GetMapping("/functional-groups/{id}")
    @Timed
    public ResponseEntity<FunctionalGroupDTO> getFunctionalGroup(@PathVariable Long id) {
        log.debug("REST request to get FunctionalGroup : {}", id);
        FunctionalGroupDTO functionalGroupDTO = functionalGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(functionalGroupDTO));
    }

    /**
     * DELETE  /functional-groups/:id : delete the "id" functionalGroup.
     *
     * @param id the id of the functionalGroupDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/functional-groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteFunctionalGroup(@PathVariable Long id) {
        log.debug("REST request to delete FunctionalGroup : {}", id);
        functionalGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
