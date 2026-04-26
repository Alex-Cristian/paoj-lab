package com.pao.proiect.audidealership.model;

public abstract class Person implements Identifiable {
    private final String id;
    private String firstName;
    private String lastName;
    private String email;

    protected Person(String id, String firstName, String lastName, String email) {
        this.id = requireText(id, "ID-ul nu poate fi gol.");
        this.firstName = requireText(firstName, "Prenumele nu poate fi gol.");
        this.lastName = requireText(lastName, "Numele nu poate fi gol.");
        this.email = requireText(email, "Email-ul nu poate fi gol.");
    }

    protected final String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }

    @Override
    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = requireText(firstName, "Prenumele nu poate fi gol.");
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = requireText(lastName, "Numele nu poate fi gol.");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = requireText(email, "Email-ul nu poate fi gol.");
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
