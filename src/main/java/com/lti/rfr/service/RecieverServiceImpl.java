package com.lti.rfr.service;

import static com.lti.rfr.config.Constants.RFR_DATE_FORMAT;
import static java.time.LocalDate.now;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lti.rfr.domain.Receiver;
import com.lti.rfr.domain.Rfr;
import com.lti.rfr.repository.FunctionalGroupRepository;
import com.lti.rfr.repository.RecieverRepository;
import com.lti.rfr.repository.RfrRepository;
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

    private final RfrRepository rfrRepo;

    private final FunctionalGroupRepository functionalGroupRepository;

    private final RecieverMapper recieverMapper;

    private final MailExchangeService emailService;

    public RecieverServiceImpl(RecieverRepository recieverRepository, RecieverMapper recieverMapper,
            FunctionalGroupRepository functionalGroupRepository, RfrRepository rfrRepo,
            MailExchangeService emailService) {
        this.recieverRepository = recieverRepository;
        this.recieverMapper = recieverMapper;
        this.functionalGroupRepository = functionalGroupRepository;
        this.rfrRepo = rfrRepo;
        this.emailService = emailService;
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

        Supplier<Stream<String>> selectedGroups = () -> recieverDTO.getGroups().stream();

        Function<Predicate<String>, List<String>> filterSelected = predicate -> selectedGroups
                .get()
                .filter(predicate)
                .collect(toList());

        recieverDTO.setGroups(selectedGroups.get().collect(toList()));
        recieverDTO.setSelectedImts(filterSelected.apply(allImts::contains));
        recieverDTO.setSelectedImt1s(filterSelected.apply(allImt1s::contains));
        recieverDTO.setSelectedImt2s(filterSelected.apply(allImt2s::contains));

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

    @Override
    public void mail(String psNumber, String psName, ReceiverDTO recieverDTO) {
        log.info("hit mailer service:: ");
        log.info("" + recieverDTO.getSelectedRfrIds());

        List<Rfr> rfrList = rfrRepo.findByRequestIdIn(recieverDTO
                .getSelectedRfrIds()
                .stream()
                .filter(Objects::nonNull)
                .map(Long::valueOf)
                .collect(toSet()));

        composeMail(psNumber, psName, rfrList);
    }

    private void composeMail(String psNumber, String psName, List<Rfr> rfrList) {

        List<Map<String, Object>> rfrRows = rfrList.stream()
                .filter(Objects::nonNull)
                .map(rfr -> {
                    Map<String, Object> dataMap = new LinkedHashMap<>();
                    String[] headers = Rfr.fetchReportHeaders();
                    String[] values = rfr.toString().split(Pattern.quote("$#$"));

                    if (headers.length > 0) {
                        IntStream.range(0, headers.length)
                                .boxed()
                                .forEach(index -> {
                                    dataMap.put(headers[index], values[index]);
                                });
                    }
                    return dataMap;
                })
                .collect(toList());

        StringBuilder htmlBody = new StringBuilder();

        htmlBody.append("Hello " + psName + ",<br><br> Please find, RFRs @ Market as of "
                + now().format(ofPattern(RFR_DATE_FORMAT))).append("<br><br>");

        htmlBody.append("<table border='1px' cellpadding='4'>");

        htmlBody.append("<thead><tr>");

        rfrRows.stream().findFirst().get().forEach((heading, value) -> {
            htmlBody.append("<th>").append(heading + "").append("</th>");
        });

        htmlBody.append("</tr></thead>");

        rfrRows.stream().forEach(row -> {

            row.remove("initiator");
            row.remove("vendor");
            row.remove("role");
            row.remove("imt");
            row.remove("imt1");
            row.remove("imt2");

            row.remove("rfpProjectModel");
            row.remove("skills");

            row.remove("fulfillment");
            row.remove("technology");
            row.remove("location");
            row.remove("onsiteHc");
            row.remove("offshoreHc");
            row.remove("status");

            row.remove("projectStartDate");
            row.remove("projectEndDate");

            htmlBody.append("<tbody><tr>");

            row.forEach((heading, value) -> {
                htmlBody.append("<td")
                        .append(heading.equals("Title")
                                || heading.equals("Apple Manager")
                                || heading.equals("Status")
                                        ? ">"
                                        : " align='center'>")
                        .append(isNotBlank(String.valueOf(value)) ? value : "")
                        .append("</td>");
            });
        });

        htmlBody.append("</tr></tbody>");
        htmlBody.append("</table>");

        emailService.sendMail("10643503", "RFR", htmlBody.toString());
    }

}
