package com.example.store.validate;

import com.example.store.exceptions.StoreIllegalArgument;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RequestValidatorTest {
    private static final String EXPECTED_MESSAGE = "Provided String value is not valid";

    @Test
    void testValidateId_happyPath() {
        assertAll(
                "valid IDs should not throw",
                () -> assertDoesNotThrow(() -> RequestValidator.validateId(1L)),
                () -> assertDoesNotThrow(() -> RequestValidator.validateId(0L)));
    }

    @Test
    void testValidateId_not_happyPath() {
        StoreIllegalArgument exception =
                assertThrows(StoreIllegalArgument.class, () -> RequestValidator.validateId(-1L));
        assertEquals("illegal ID provided", exception.getMessage());
    }

    @Test
    void testValidateString_happyPath() {
        assertAll(
                () -> assertDoesNotThrow(() -> RequestValidator.validateString("Hello")),
                () -> assertDoesNotThrow(() -> RequestValidator.validateString("a".repeat(255))));
    }

    @ParameterizedTest
    @MethodSource("inputValue")
    void testValidateString_not_happyPath(String input) {
        StoreIllegalArgument exception =
                assertThrows(StoreIllegalArgument.class, () -> RequestValidator.validateString(input));
        assertEquals(EXPECTED_MESSAGE, exception.getMessage());
    }

    private static Stream<String> inputValue() {
        return Stream.of(null, "", "a".repeat(256));
    }
}
