package com.incdocs.utils;

import com.incdocs.model.constants.ApplicationConstants;
import org.springframework.http.HttpStatus;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class UriUtils {
    private static final String INCDOCS_ID = "incdocsID";

    static public final String[] userDetailsFromAuthHeader(HttpServletRequest request)
            throws ApplicationException{
        String authorization = request.getHeader("Authorization");
        String [] authParts = authorization.split("\\s+");
        String authInfo = authParts[1];
        byte[] bytes = null;
        try {
            bytes = new BASE64Decoder().decodeBuffer(authInfo);
        } catch (IOException e) {
            throw new ApplicationException(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        String decodeAuth = new String(bytes);
        return decodeAuth.split(":");
    }

    static public final String userIdFromAuthHeader(HttpServletRequest request)
            throws ApplicationException {
        String [] authDetails = userDetailsFromAuthHeader(request);
        return authDetails[0];
    }
}
