package com.incdocs.config.interceptor;

import com.incdocs.model.constants.ApplicationConstants;
import com.incdocs.user.helper.UserManagementHelper;
import com.incdocs.utils.ApplicationException;
import com.incdocs.model.domain.User;
import com.incdocs.utils.UriUtils;
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
        System.out.println("AuthInterceptor: authenticating user: " + incdocsID);
        User user = userManagementHelper.getUser(incdocsID);
        if (user == null) {
           throw new ApplicationException("invalid credentials", HttpStatus.UNAUTHORIZED);
        }
        if (user.getStatus() != ApplicationConstants.UserStatus.ACTIVE) {
            throw new ApplicationException("user not active", HttpStatus.UNAUTHORIZED);
        }
        return true;
    }
}
