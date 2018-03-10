package com.incdocs.config.interceptor;

import com.incdocs.user.helper.UserManagementHelper;
import com.incdocs.utils.ApplicationException;
import com.indocs.model.response.UserEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.indocs.model.constants.ApplicationConstants.RequestHeaders.ID;

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
        UserEntity userEntity = userManagementHelper.getUserRolesActions(incdocsID);
        return userEntity != null;
    }
}
