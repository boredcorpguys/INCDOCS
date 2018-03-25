package com.incdocs.user.validator;

import com.incdocs.utils.ApplicationException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserServiceValidator {

/*    @Before("execution(* com.incdocs.user.services.AdminService.createUser(..))")
    public void validateModifyUser(JoinPoint joinPoint) throws ApplicationException {

    }*/
}
