package com.murilo.market_place.exception;

public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(Class<?> specifiedClass) {
        super(String.format("No %s related parameters specified parameters", specifiedClass.getName()));
    }

    public ObjectNotFoundException(String message) {
        super(message);
    }
}
