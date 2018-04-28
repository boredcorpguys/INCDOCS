package com.incdocs.config.interceptor;

import com.incdocs.model.constants.ApplicationConstants;
import com.incdocs.model.domain.User;
import com.incdocs.user.helper.UserManagementHelper;
import com.incdocs.utils.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.incdocs.model.constants.ApplicationConstants.RequestHeaders.ID;

@Component("authInterceptor")
public class AuthInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

    @Autowired
    @Qualifier("userManagementHelper")
    private UserManagementHelper userManagementHelper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String incdocsID = request.getHeader(ID.getName());
        logger.info("AuthInterceptor: authenticating user: " + incdocsID);
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
