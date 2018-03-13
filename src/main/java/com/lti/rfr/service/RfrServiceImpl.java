package com.lti.rfr.service;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toSet;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.lti.rfr.domain.Rfr;
import com.lti.rfr.repository.RfrRepository;
import com.lti.rfr.service.dto.RfrRaw;

@Service
public class RfrServiceImpl implements RfrService {

    private final Logger log = LoggerFactory.getLogger(RfrServiceImpl.class);

    private final RfrRepository rfrRepository;

    public RfrServiceImpl(RfrRepository rfrRepository) {
        this.rfrRepository = rfrRepository;
    }

    @Override
    public void importRfr(List<Map<String, String>> rows) {

        log.info("Rfr Service Impl Invoked:::");

        rfrRepository.save(rows.stream()
                .skip(1) // header
                .map(RfrRaw::new)
                .map(Rfr::new)
                .collect(toSet()));

        log.info("importing::saved");
    }

    @Override
    public List<Rfr> getAll() {
        return rfrRepository.findAll();
    }

    @Override
    public Optional<Rfr> getById(Long requestId) {
        return ofNullable(rfrRepository.findOne(requestId));
    }

    @Override
    public Rfr update(RfrRaw rfrRaw) {
        return rfrRepository.save(new Rfr(rfrRaw));
    }

    @Override
    public Set<String> gettAllIdsByImtxGroup(List<String> imts, List<String> imt1s, List<String> imt2s) {

        List<Rfr> imtRfrs = rfrRepository.findByImtIn(imts);
        List<Rfr> imt1Rfrs = rfrRepository.findByImtIn(imts);
        List<Rfr> imt2Rfrs = rfrRepository.findByImtIn(imts);

        Function<List<Rfr>, Set<String>> getIdSet = rfrList -> imtRfrs.stream()
                .map(Rfr::getRequestId)
                .map(String::valueOf)
                .collect(toSet());

        Set<String> allRfrIds = new HashSet<>();

        allRfrIds.addAll(getIdSet.apply(imtRfrs));
        allRfrIds.addAll(getIdSet.apply(imt1Rfrs));
        allRfrIds.addAll(getIdSet.apply(imt2Rfrs));

        return allRfrIds;
    }

}
