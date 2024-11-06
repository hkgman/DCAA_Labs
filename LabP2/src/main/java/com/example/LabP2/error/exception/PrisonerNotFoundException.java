package com.example.LabP2.error.exception;

import java.util.UUID;

public class PrisonerNotFoundException extends RuntimeException {
    public PrisonerNotFoundException(UUID id) {
        super(String.format("prisoner with id [%s] is not found", id));
    }
}
