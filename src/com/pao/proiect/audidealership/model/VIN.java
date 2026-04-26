package com.pao.proiect.audidealership.model;

import com.pao.proiect.audidealership.exception.InvalidVinException;

import java.util.Objects;

public final class VIN {
    private static final int VIN_LENGTH = 17;
    private final String value;

    public VIN(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidVinException("VIN-ul nu poate fi null sau gol.");
        }

        String normalized = value.trim().toUpperCase();
        if (normalized.length() != VIN_LENGTH) {
            throw new InvalidVinException("VIN-ul trebuie sa aiba exact 17 caractere.");
        }

        if (!normalized.matches("[A-HJ-NPR-Z0-9]{17}")) {
            throw new InvalidVinException("VIN-ul contine caractere invalide.");
        }

        this.value = normalized;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VIN vin)) {
            return false;
        }
        return Objects.equals(value, vin.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
