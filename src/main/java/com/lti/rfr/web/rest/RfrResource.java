package com.lti.rfr.web.rest;

import static java.time.LocalDate.now;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lti.rfr.domain.Rfr;
import com.lti.rfr.service.RfrService;
import com.lti.rfr.service.dto.RfrRaw;

@RestController
@RequestMapping("/api/rfr")
public class RfrResource {

    private final Logger log = LoggerFactory.getLogger(RfrResource.class);

    private final RfrService rfrService;

    public RfrResource(RfrService rfrService) {
        this.rfrService = rfrService;
    }

    @GetMapping
    public ResponseEntity<List<Rfr>> getAll(Pageable pageable) {
        return ok(rfrService.getAll());
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Rfr> getById(@PathVariable Long requestId) {
        return ok(rfrService.getById(requestId).orElseThrow(() -> new RuntimeException("RFR not found")));
    }

    @PutMapping
    public ResponseEntity<Rfr> update(@RequestBody RfrRaw rfrRaw) {
        return ok(rfrService.update(rfrRaw));
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        
        Workbook workbook = rfrService.export();

        response.setHeader("Content-Disposition", "attachment; filename=LTI_RFx_Data_" + now() + ".xlsx");
        response.setHeader("Set-Cookie", "fileDownload=true; path=/");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteAll() {
        try {
            rfrService.deleteAll();
            return ok().build();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return status(INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping(path = "/{requestId}")
    public ResponseEntity<Object> deleteOne() {
        return null;
    }

}
