package com.lti.rfr.util;

import static com.lti.rfr.config.Constants.CSV;
import static com.lti.rfr.config.Constants.DELIMITER;
import static com.lti.rfr.config.Constants.EV_HSSF;
import static com.lti.rfr.config.Constants.EV_XSSF;
import static com.lti.rfr.config.Constants.FORMATTER;
import static com.lti.rfr.config.Constants.NA;
import static com.lti.rfr.config.Constants.XLS;
import static com.lti.rfr.config.Constants.XLSX;
import static com.lti.rfr.util.RfrUtil.toStream;
import static com.monitorjbl.xlsx.StreamingReader.builder;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;
import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.csv.CSVFormat.EXCEL;
import static org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted;
import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.RETURN_NULL_AND_BLANK;
import static org.apache.poi.ss.usermodel.WorkbookFactory.create;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.csv.CSVRecord;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilities for handling .xlsx, .xlx, .csv formats
 * 
 * @author Sharan Arumugam
 * @version 3.0
 *
 */
public class ExcelUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * Row Stream with StreamingReader for .xlsx - compatible with large file
     * 
     * @param inputStream
     * @return list of row map - columns as keys
     */
    public static final List<Map<String, String>> parseXlsx(InputStream inputStream) {
        return asListMap(
                toStream(builder()
                        .open(inputStream)
                        .getSheetAt(0)
                        .iterator()),
                EV_XSSF);
    }

    /**
     * Parse .xls (1997-2004) with vanilla POI, not compatible with large file
     * 
     * @param inputStream
     * @return list of row map - columns as keys
     */
    public static final List<Map<String, String>> parseXls(InputStream inputStream) {
        try {
            return asListMap(
                    toStream(create(inputStream)
                            .getSheetAt(0)
                            .iterator()),
                    EV_HSSF);

        } catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * IputStream to list map of column as keys
     * 
     * @param rowStream
     * @param evaluator
     * @return list of row map - columns as keys
     */
    @SuppressWarnings("deprecation")
    private static final List<Map<String, String>> asListMap(Stream<Row> rowStream, FormulaEvaluator evaluator) {

        List<String> rowValueList = rowStream.map(row -> {

            row.getSheet().getWorkbook().setMissingCellPolicy(RETURN_NULL_AND_BLANK);

            return toStream(row.iterator()).map(cell -> {

                switch (cell.getCellTypeEnum()) {

                case BLANK:
                    return " ";

                case STRING:
                    return cell.getStringCellValue();

                case NUMERIC:
                    return isCellDateFormatted(cell)
                            ? String.valueOf(cell.getDateCellValue())
                            : FORMATTER.formatCellValue(cell, evaluator);

                default:
                    return null;
                }
            }).collect(joining(DELIMITER));

        }).collect(toList());

        int headerRow = 0;

        String[] headers = rowValueList.get(headerRow).split(DELIMITER);

        return rowValueList.stream()
                .skip(headerRow)
                .map(row -> row.split(DELIMITER))
                .map(row -> {
                    return range(0, row.length)
                            .boxed()
                            .collect(toMap(column -> headers[column].trim(), column -> row[column].trim()));
                })
                .collect(toList());
    }

    /**
     * Parse CSV to list of row map - column as keys
     * 
     * @param inputStream
     * @return list of row map - columns as keys
     */
    public static final List<Map<String, String>> parseCsv(InputStream inputStream) {
        try {
            Reader reader = new InputStreamReader(inputStream);
            return stream(EXCEL
                    .withHeader()
                    .parse(reader)
                    .spliterator(), false)
                            .map(CSVRecord::toMap)
                            .collect(toList());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Stream workbook to byte array
     * 
     * @param workbook
     * @return byte[] of workbook
     */
    public static final byte[] toByteArray(Workbook workbook) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            workbook.write(outputStream);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return outputStream.toByteArray();
    }

    /**
     * get file extension [.csv | .xls | .xlsx]
     */
    public static final String getFileExtension(String fileName) {
        fileName = fileName.toLowerCase();
        String ext = NA;
        if (fileName.endsWith(CSV)) {
            ext = CSV;
        } else if (fileName.endsWith(XLSX)) {
            ext = XLSX;
        } else if (fileName.endsWith(XLS)) {
            ext = XLS;
        }
        return ext;
    }

    /**
     * Parse by extension .csv | .xls | .xlsx
     * 
     * @param fileName
     * @param inputStream
     * @return
     */
    public static final List<Map<String, String>> parseAny(String fileName, InputStream inputStream) {
        switch (getFileExtension(fileName)) {
        case CSV:
            return parseCsv(inputStream);
        case XLSX:
            return parseXlsx(inputStream);
        case XLS:
            return parseXls(inputStream);
        default:
            throw new RuntimeException("Unsupported file type, try .csv, .xls or .xlsx; large files must be .xlsx");
        }
    }
}
