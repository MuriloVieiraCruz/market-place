package com.murilo.market_place.exception;

public class NullValueInsertionException extends RuntimeException {

    public NullValueInsertionException(String message) {
        super(message);
    }

    public NullValueInsertionException() {
        super();
    }
}
