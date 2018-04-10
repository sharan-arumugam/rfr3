package com.lti.rfr.service;

import static com.lti.rfr.util.RfrUtil.splitCamelCase;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.math.NumberUtils.isCreatable;
import static org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
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

        Function<List<Rfr>, Set<String>> getIdSet = list -> list.stream()
                .map(Rfr::getRequestId)
                .map(String::valueOf)
                .collect(toSet());

        Set<String> allRfrIds = new HashSet<>();

        allRfrIds.addAll(getIdSet.apply(rfrRepository.findByImtIn(imts)));
        allRfrIds.addAll(getIdSet.apply(rfrRepository.findByImt1In(imt1s)));
        allRfrIds.addAll(getIdSet.apply(rfrRepository.findByImt2In(imt2s)));

        return allRfrIds;
    }

    @Override
    public Workbook export() {
        try {
            List<Rfr> rfrList = rfrRepository.findAll();
            return getReport(rfrList);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public Workbook getReport(List<Rfr> rfrList) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        Rfr headerSampleRfr = rfrList.stream().findFirst().get();
        Map<String, Object> headerMap = mapper.readValue(mapper.writeValueAsString(headerSampleRfr), Map.class);

        return getRfrWorkBook(new XSSFWorkbook(), headerMap, rfrList);
    }

    Workbook getRfrWorkBook(Workbook workbook, Map<String, Object> headerMap, List<Rfr> rfrList) {

        Font font = workbook.createFont();
        font.setBold(true);
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(CENTER);
        headerStyle.setFont(font);

        ObjectMapper mapper = new ObjectMapper();

        Sheet sheet = workbook.createSheet();

        AtomicInteger headerColCount = new AtomicInteger();
        AtomicInteger rowCount = new AtomicInteger();

        Row headerRow = sheet.createRow(rowCount.getAndIncrement());

        headerMap.entrySet().stream().forEach(entry -> {
            Cell cell = headerRow.createCell(headerColCount.getAndIncrement());
            cell.setCellValue(splitCamelCase(entry.getKey()));
            cell.setCellStyle(headerStyle);
        });

        rfrList.forEach(rfr -> {
            try {
                Row row = sheet.createRow(rowCount.getAndIncrement());
                AtomicInteger colCount = new AtomicInteger();
                Map<String, Object> rowMap = mapper.readValue(mapper.writeValueAsString(rfr), Map.class);

                rowMap.values().forEach(value -> {
                    Cell cell = row.createCell(colCount.getAndIncrement());

                    if (null != value && isCreatable(String.valueOf(value))) {
                        cell.setCellValue(Double.valueOf(String.valueOf(value)));

                    } else {
                        cell.setCellValue(null != value ? String.valueOf(value) : "");
                    }
                });

                CellRangeAddress rangeAddress = CellRangeAddress.valueOf("A1:X1");
                sheet.setAutoFilter(rangeAddress);

                int autoSizeCount = 0;

                for (Object headKey : headerMap.values()) {
                    String heading = String.valueOf(headKey);

                    int length = heading.length() > 12 ? heading.length() + 3 : 12;
                    length = "Request Title".equals(heading) ? 25 : length;

                    if (heading.contains("Imt") || heading.equals("Imt")) {
                        length = 25;
                    }

                    int width = ((int) (length * 1.14388)) * 256;
                    sheet.setColumnWidth(autoSizeCount++, width);
                }

            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        });

        return workbook;
    }

    @Override
    public void deleteAll() {
        log.info("deleteAll::service");
        rfrRepository.deleteAll();
    }

}
