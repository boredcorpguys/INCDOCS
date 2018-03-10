package com.incdocs.utils;

public interface Validator<T> {
    void validate(T obj) throws ApplicationException;
}
