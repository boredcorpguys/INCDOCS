package com.incdocs.config.interceptor;

import com.incdocs.model.constants.ApplicationConstants;
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

import java.io.PrintWriter;

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
        if (user == null) {
            throw new ApplicationException(String.format(("invalid credentials")), HttpStatus.UNAUTHORIZED);
        }
        if (user.getStatus() != ApplicationConstants.UserStatus.ACTIVE) {
            throw new ApplicationException(String.format(("user not active")), HttpStatus.UNAUTHORIZED);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (ex != null) {
            if (ex instanceof ApplicationException) {
                response.setStatus(((ApplicationException) ex).getHttpStatusCode().value());
            } else {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
            }
            PrintWriter writer = response.getWriter();
            writer.println(String.format("HTTP Status %s : %s",String.valueOf(response.getStatus()), ex.getMessage()));
        }
    }
}
