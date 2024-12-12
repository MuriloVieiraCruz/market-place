package com.murilo.market_place.domains.enums;

public enum RoleValue {

    BASIC(0),
    ADMIN(1);

    int roleId;

    private final int code;

    RoleValue(int code) {
        this.code = code;
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
