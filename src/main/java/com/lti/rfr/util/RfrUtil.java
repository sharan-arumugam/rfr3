package com.lti.rfr.util;

import static com.lti.rfr.config.Constants.RFR_DATE_FORMAT;
import static java.lang.Long.parseLong;
import static java.time.LocalDate.parse;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.join;
import static org.apache.commons.lang3.StringUtils.splitByCharacterTypeCamelCase;
import static org.apache.commons.lang3.math.NumberUtils.isCreatable;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

public final class RfrUtil {

    private RfrUtil() {
    }

    public static String splitCamelCase(String inputString) {
        return capitalize(join(splitByCharacterTypeCamelCase(inputString), " "));
    }

    public static final Map<String, String> RFR_FIELDS = new HashMap<String, String>();
    static {
        RFR_FIELDS.put("requestId", "Request ID");
        RFR_FIELDS.put("status", "Status");
        RFR_FIELDS.put("requestTitle", "Request Title");
        RFR_FIELDS.put("requestType", "Request Type");
        RFR_FIELDS.put("businessType", "Business Type");
        RFR_FIELDS.put("sendDate", "Send Date");
        RFR_FIELDS.put("dueDate", "Due Date");
        RFR_FIELDS.put("appleManager", "Apple Manager");
        RFR_FIELDS.put("initiator", "Initiator");
        RFR_FIELDS.put("vendor", "Vendor");
        RFR_FIELDS.put("role", "Role");
        RFR_FIELDS.put("imt", "IMT");
        RFR_FIELDS.put("imt1", "IMT-1");
        RFR_FIELDS.put("imt2", "IMT-2");
        RFR_FIELDS.put("rfpProjectType", "RFP Project Type");
        RFR_FIELDS.put("rfpProjectModel", "RFP Project Model");
        RFR_FIELDS.put("projectStartDate", "Project Start Date");
        RFR_FIELDS.put("projectEndDate", "Project End Date");
    }

    public static final LocalDate parseRfrDate(String dateString) {
        return isNotBlank(dateString)
                ? parse(dateString, ofPattern(RFR_DATE_FORMAT))
                : null;
    }

    public static final String formatRfrDate(LocalDate localDate) {
        return null != localDate
                ? localDate.format(ofPattern(RFR_DATE_FORMAT))
                : null;
    }

    public static final Long parseLongString(String longString) {
        return isCreatable(longString) ? parseLong(longString) : null;
    }

    public static final <T> Stream<T> toStream(Iterator<T> iterator) {
        Iterable<T> iterable = () -> iterator;
        return stream(iterable.spliterator(), false);
    }
}
