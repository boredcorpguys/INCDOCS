package com.incdocs.config.interceptor;

import com.incdocs.user.helper.UserManagementHelper;
import com.indocs.model.response.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.indocs.model.constants.ApplicationConstants.RequestHeaders.ID;
import static com.indocs.model.constants.ApplicationConstants.RequestHeaders.ORIGIN;

@Component("authInterceptor")
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    @Qualifier("userManagementHelper")
    private UserManagementHelper userManagementHelper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String incdocsID = request.getHeader(ID.getName());
        System.out.println("AuthInterceptor: authenticating user: " + incdocsID);
        UserEntity userEntity = userManagementHelper.getUserRolesActions(incdocsID);
        return userEntity != null;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        response.setHeader(ORIGIN.getName(), "*");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        response.setHeader(ORIGIN.getName(), "*");
    }
}
