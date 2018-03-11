package com.incdocs.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

public class Utils {
    public static String idGenerator(String companyID, String empID) {
        return empID.toLowerCase() + "_" + companyID.toLowerCase();
    }

    public static <T> void validate(T field, String fieldName) {
        boolean valid = true;
        if (field instanceof String)
            valid = StringUtils.isNotEmpty((String)field);
        else
            valid = (field != null);
        if (!valid)
            throw new IllegalArgumentException(fieldName+" is null");
    }

    public static <T> void validate(T field, String message, HttpStatus statusCode)
            throws ApplicationException{
        try{
            validate(field, "");
        }
        catch (IllegalArgumentException ie) {
            throw new ApplicationException(message, statusCode);
        }
    }
}
