package com.lti.rfr.web.rest;

import static org.springframework.http.ResponseEntity.ok;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lti.rfr.domain.Rfr;
import com.lti.rfr.service.RfrService;
import com.lti.rfr.service.dto.RfrRaw;

@RestController
@RequestMapping("/api")
public class RfrResource {

    private final Logger log = LoggerFactory.getLogger(RfrResource.class);

    private final RfrService rfrService;

    public RfrResource(RfrService rfrService) {
        this.rfrService = rfrService;
    }

    @GetMapping("/rfr")
    public ResponseEntity<List<Rfr>> getAll(Pageable pageable) {
        log.info("invoking getAll in Controller");
        return ok(rfrService.getAll());
    }

    @GetMapping("/rfr/{requestId}")
    public ResponseEntity<Rfr> getById(@PathVariable Long requestId) {
        log.info("invoking getById in Controller :: " + requestId);
        return ok(rfrService.getById(requestId).orElseThrow(() -> new RuntimeException("RFR not found")));
    }

    @PutMapping("/rfr")
    public ResponseEntity<Rfr> update(@RequestBody RfrRaw rfrRaw) {
        log.info("rfrRaw:: " + rfrRaw);
        return ok(rfrService.update(rfrRaw));
    }

    @PatchMapping("/rfr")
    public ResponseEntity<Object> export() {
        log.info("export:: ");
        return ok(rfrService.export());
    }

}
