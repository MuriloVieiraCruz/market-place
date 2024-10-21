package com.murilo.market_place.exception.enums;

import lombok.Getter;

@Getter
public enum ApiError {

    INVALID_BODY("001"),
    INVALID_FORMAT("002"),
    INVALID_PARAMETER("003"),
    REQUIRED_PRECONDITION("004"),
    INVALID_TYPE_PARAMETER("005"),
    HTTP_METHOD_NOT_SUPPORTED("006"),
    MANDATORY_PARAMETER("007"),
    INVALID_FIELD("008");

    private final String code;

    ApiError(String code){
        this.code = code;
    }
}
