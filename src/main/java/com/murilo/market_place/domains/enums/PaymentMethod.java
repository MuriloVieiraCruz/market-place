package com.murilo.market_place.domains.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    CREDIT_CARD(0, "Credit Card"),
    FETLOCK(1, "FeatLock"),
    PIX(2, "Pix");

    private final int code;
    private final String description;

    PaymentMethod(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static PaymentMethod fromCode(int code) {
        for (PaymentMethod method : PaymentMethod.values()) {
            if (method.getCode() == code) {
                return method;
            }
        }

        throw new IllegalArgumentException("Invalid payment method: " + code);
    }
}
