package com.murilo.market_place.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class<?> specifiedClass) {
        super(String.format("No %s found with the reported parameters", specifiedClass.getName()));
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException() {
        super();
    }
}
