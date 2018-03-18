package com.incdocs.config.interceptor;

import com.incdocs.user.helper.UserManagementHelper;
import com.incdocs.utils.ApplicationException;
import com.incdocs.model.domain.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.incdocs.model.constants.ApplicationConstants.RequestHeaders.ID;

@Component("authInterceptor")
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    @Qualifier("userManagementHelper")
    private UserManagementHelper userManagementHelper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String incdocsID = request.getHeader(ID.getName());
        if (StringUtils.isEmpty(incdocsID)) {
            throw new ApplicationException(String.format("%s not set in request header", ID.getName())
                    , HttpStatus.BAD_REQUEST);
        }
        System.out.println("AuthInterceptor: authenticating user: " + incdocsID);
        User user = userManagementHelper.getUser(incdocsID);
        return user != null;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (ex != null) {
            if (ex instanceof DuplicateKeyException) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                throw new ApplicationException("Duplicate value error", HttpStatus.BAD_REQUEST);
            } else
                throw new ApplicationException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
