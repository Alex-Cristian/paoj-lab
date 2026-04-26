package com.pao.proiect.audidealership.model;

import java.util.Objects;

public class Customer extends Person {
    private String phoneNumber;
    private String loyaltyTier;

    public Customer(String id, String firstName, String lastName, String email, String phoneNumber, String loyaltyTier) {
        super(id, firstName, lastName, email);
        this.phoneNumber = requireText(phoneNumber, "Numarul de telefon nu poate fi gol.");
        this.loyaltyTier = requireText(loyaltyTier, "Nivelul de fidelitate nu poate fi gol.");
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = requireText(phoneNumber, "Numarul de telefon nu poate fi gol.");
    }

    public String getLoyaltyTier() {
        return loyaltyTier;
    }

    public void setLoyaltyTier(String loyaltyTier) {
        this.loyaltyTier = requireText(loyaltyTier, "Nivelul de fidelitate nu poate fi gol.");
    }

    @Override
    public String toString() {
        return "Customer{id='%s', name='%s', email='%s', phone='%s', loyaltyTier='%s'}"
                .formatted(getId(), getFullName(), getEmail(), phoneNumber, loyaltyTier);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer customer)) {
            return false;
        }
        return Objects.equals(getId(), customer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
