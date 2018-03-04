package com.lti.rfr.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lti.rfr.service.EmailAddressService;
import com.lti.rfr.web.rest.errors.BadRequestAlertException;
import com.lti.rfr.web.rest.util.HeaderUtil;
import com.lti.rfr.web.rest.util.PaginationUtil;
import com.lti.rfr.service.dto.EmailAddressDTO;
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
 * REST controller for managing EmailAddress.
 */
@RestController
@RequestMapping("/api")
public class EmailAddressResource {

    private final Logger log = LoggerFactory.getLogger(EmailAddressResource.class);

    private static final String ENTITY_NAME = "emailAddress";

    private final EmailAddressService emailAddressService;

    public EmailAddressResource(EmailAddressService emailAddressService) {
        this.emailAddressService = emailAddressService;
    }

    /**
     * POST  /email-addresses : Create a new emailAddress.
     *
     * @param emailAddressDTO the emailAddressDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new emailAddressDTO, or with status 400 (Bad Request) if the emailAddress has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/email-addresses")
    @Timed
    public ResponseEntity<EmailAddressDTO> createEmailAddress(@RequestBody EmailAddressDTO emailAddressDTO) throws URISyntaxException {
        log.debug("REST request to save EmailAddress : {}", emailAddressDTO);
        if (emailAddressDTO.getId() != null) {
            throw new BadRequestAlertException("A new emailAddress cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmailAddressDTO result = emailAddressService.save(emailAddressDTO);
        return ResponseEntity.created(new URI("/api/email-addresses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /email-addresses : Updates an existing emailAddress.
     *
     * @param emailAddressDTO the emailAddressDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated emailAddressDTO,
     * or with status 400 (Bad Request) if the emailAddressDTO is not valid,
     * or with status 500 (Internal Server Error) if the emailAddressDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/email-addresses")
    @Timed
    public ResponseEntity<EmailAddressDTO> updateEmailAddress(@RequestBody EmailAddressDTO emailAddressDTO) throws URISyntaxException {
        log.debug("REST request to update EmailAddress : {}", emailAddressDTO);
        if (emailAddressDTO.getId() == null) {
            return createEmailAddress(emailAddressDTO);
        }
        EmailAddressDTO result = emailAddressService.save(emailAddressDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, emailAddressDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /email-addresses : get all the emailAddresses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of emailAddresses in body
     */
    @GetMapping("/email-addresses")
    @Timed
    public ResponseEntity<List<EmailAddressDTO>> getAllEmailAddresses(Pageable pageable) {
        log.debug("REST request to get a page of EmailAddresses");
        Page<EmailAddressDTO> page = emailAddressService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/email-addresses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /email-addresses/:id : get the "id" emailAddress.
     *
     * @param id the id of the emailAddressDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the emailAddressDTO, or with status 404 (Not Found)
     */
    @GetMapping("/email-addresses/{id}")
    @Timed
    public ResponseEntity<EmailAddressDTO> getEmailAddress(@PathVariable Long id) {
        log.debug("REST request to get EmailAddress : {}", id);
        EmailAddressDTO emailAddressDTO = emailAddressService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(emailAddressDTO));
    }

    /**
     * DELETE  /email-addresses/:id : delete the "id" emailAddress.
     *
     * @param id the id of the emailAddressDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/email-addresses/{id}")
    @Timed
    public ResponseEntity<Void> deleteEmailAddress(@PathVariable Long id) {
        log.debug("REST request to delete EmailAddress : {}", id);
        emailAddressService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
