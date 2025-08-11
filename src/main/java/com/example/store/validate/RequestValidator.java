package com.example.store.validate;

import com.example.store.exceptions.StoreIllegalArgument;

public class RequestValidator {
    public static void validateId(Long id) throws StoreIllegalArgument {
        if (id < 0) {
            throw new StoreIllegalArgument("illegal ID provided");
        }
    }

    public static void validateString(String value) throws StoreIllegalArgument {
        if (value == null || value.isEmpty() || value.length() > 255) {
            throw new StoreIllegalArgument("Provided String value is not valid");
        }
    }

    private RequestValidator() {}
}
