package com.murilo.market_place.domains.enums;

import lombok.Getter;

@Getter
public enum Status {

    ACTIVATED(0, "Activated"),
    WAITING_ACTIVATION(1, "Waiting_Activation"),
    DEACTIVATED(2, "Deactivated");

    private final int code;
    private final String description;

    Status(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static Status fromCode(int code) {
        for (Status method : Status.values()) {
            if (method.getCode() == code) {
                return method;
            }
        }

        throw new IllegalArgumentException("Invalid payment method: " + code);
    }
}
