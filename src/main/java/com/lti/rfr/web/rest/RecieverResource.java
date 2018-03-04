package com.lti.rfr.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lti.rfr.service.RecieverService;
import com.lti.rfr.web.rest.errors.BadRequestAlertException;
import com.lti.rfr.web.rest.util.HeaderUtil;
import com.lti.rfr.web.rest.util.PaginationUtil;
import com.lti.rfr.service.dto.RecieverDTO;
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
 * REST controller for managing Reciever.
 */
@RestController
@RequestMapping("/api")
public class RecieverResource {

    private final Logger log = LoggerFactory.getLogger(RecieverResource.class);

    private static final String ENTITY_NAME = "reciever";

    private final RecieverService recieverService;

    public RecieverResource(RecieverService recieverService) {
        this.recieverService = recieverService;
    }

    /**
     * POST  /recievers : Create a new reciever.
     *
     * @param recieverDTO the recieverDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new recieverDTO, or with status 400 (Bad Request) if the reciever has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/recievers")
    @Timed
    public ResponseEntity<RecieverDTO> createReciever(@RequestBody RecieverDTO recieverDTO) throws URISyntaxException {
        log.debug("REST request to save Reciever : {}", recieverDTO);
        if (recieverDTO.getId() != null) {
            throw new BadRequestAlertException("A new reciever cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RecieverDTO result = recieverService.save(recieverDTO);
        return ResponseEntity.created(new URI("/api/recievers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /recievers : Updates an existing reciever.
     *
     * @param recieverDTO the recieverDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated recieverDTO,
     * or with status 400 (Bad Request) if the recieverDTO is not valid,
     * or with status 500 (Internal Server Error) if the recieverDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/recievers")
    @Timed
    public ResponseEntity<RecieverDTO> updateReciever(@RequestBody RecieverDTO recieverDTO) throws URISyntaxException {
        log.debug("REST request to update Reciever : {}", recieverDTO);
        if (recieverDTO.getId() == null) {
            return createReciever(recieverDTO);
        }
        RecieverDTO result = recieverService.save(recieverDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, recieverDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /recievers : get all the recievers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of recievers in body
     */
    @GetMapping("/recievers")
    @Timed
    public ResponseEntity<List<RecieverDTO>> getAllRecievers(Pageable pageable) {
        log.debug("REST request to get a page of Recievers");
        Page<RecieverDTO> page = recieverService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/recievers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /recievers/:id : get the "id" reciever.
     *
     * @param id the id of the recieverDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the recieverDTO, or with status 404 (Not Found)
     */
    @GetMapping("/recievers/{id}")
    @Timed
    public ResponseEntity<RecieverDTO> getReciever(@PathVariable Long id) {
        log.debug("REST request to get Reciever : {}", id);
        RecieverDTO recieverDTO = recieverService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(recieverDTO));
    }

    /**
     * DELETE  /recievers/:id : delete the "id" reciever.
     *
     * @param id the id of the recieverDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/recievers/{id}")
    @Timed
    public ResponseEntity<Void> deleteReciever(@PathVariable Long id) {
        log.debug("REST request to delete Reciever : {}", id);
        recieverService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
