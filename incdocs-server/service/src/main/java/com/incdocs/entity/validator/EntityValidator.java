package com.incdocs.entity.validator;

import com.incdocs.utils.ApplicationException;
import com.incdocs.utils.Validator;
import com.indocs.model.domain.Entity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component("entityValidator")
public class EntityValidator implements Validator<Entity> {
    @Override
    public void validate(Entity obj) throws ApplicationException {
        if (Objects.nonNull(obj))
            throw new ApplicationException("entity not setup in incdocs",
                    HttpStatus.BAD_REQUEST);
    }
}
