package com.murilo.market_place.exception;

public class NullInsertValueException extends RuntimeException {

    public NullInsertValueException(String message) {
        super(message);
    }

    public NullInsertValueException() {
        super();
    }
}
