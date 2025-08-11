package com.example.store.aspect;

import com.example.store.exceptions.ErrorResponse;
import com.example.store.exceptions.StoreIllegalArgument;
import com.example.store.exceptions.StoreValueNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

        private GlobalExceptionHandler handler;

        @BeforeEach
        void setUp() {
            handler = new GlobalExceptionHandler();
        }

        @Test
        void handleGenericException_returnsInternalServerError() {
            Exception ex = new RuntimeException("Boom!");

            ResponseEntity<ErrorResponse> response = handler.handleException(ex);

            assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().getMessage().contains("Boom!"));
            assertEquals("GENERIC_ERROR", response.getBody().getCode());
            assertNotNull(response.getBody().getTimestamp());
        }

        @Test
        void handleValueNotFoundException_returnsNotFound() {
            StoreValueNotFound ex = new StoreValueNotFound("Key not found");

            ResponseEntity<ErrorResponse> response = handler.handleValueNotFoundException(ex);

            assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Key not found", response.getBody().getMessage());
            assertEquals("NOT_FOUND", response.getBody().getCode());
            assertNotNull(response.getBody().getTimestamp());
        }

        @Test
        void handleIllegalArgument_returnsBadRequest() {
            StoreIllegalArgument ex = new StoreIllegalArgument("Invalid input");

            ResponseEntity<ErrorResponse> response = handler.illegalArgument(ex);

            assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Invalid input", response.getBody().getMessage());
            assertEquals("VALIDATION_ERROR", response.getBody().getCode());
            assertNotNull(response.getBody().getTimestamp());
        }

}