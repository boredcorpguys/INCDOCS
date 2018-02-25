package com.incdocs.user.validator;

import com.incdocs.utils.ApplicationException;
import com.incdocs.utils.Validator;
import com.indocs.model.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Predicate;

@Component("userValidator")
public class UserValidator implements Validator<User>{

    @Override
    public void validate(User user) throws ApplicationException {
        if (Objects.nonNull(user))
            throw new ApplicationException("user not setup in incdocs",
                    HttpStatus.BAD_REQUEST);
    }
}
