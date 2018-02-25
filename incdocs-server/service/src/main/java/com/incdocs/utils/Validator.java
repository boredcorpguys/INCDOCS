package com.incdocs.utils;

import java.util.function.Predicate;

public interface Validator<T> {
   void validate(T obj) throws ApplicationException;
}
