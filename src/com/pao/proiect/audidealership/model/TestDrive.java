package com.pao.proiect.audidealership.model;

import java.time.LocalDateTime;

public class TestDrive implements Identifiable {
    private final String id;
    private final Customer customer;
    private final AudiCar car;
    private final LocalDateTime dateTime;

    public TestDrive(String id, Customer customer, AudiCar car, LocalDateTime dateTime) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID-ul test drive-ului nu poate fi gol.");
        }
        if (customer == null || car == null || dateTime == null) {
            throw new IllegalArgumentException("Datele test drive-ului nu pot fi null.");
        }
        this.id = id.trim();
        this.customer = customer;
        this.car = car;
        this.dateTime = dateTime;
    }

    @Override
    public String getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public AudiCar getCar() {
        return car;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        return "TestDrive{id='%s', customer='%s', vin='%s', dateTime=%s}"
                .formatted(id, customer.getFullName(), car.getVin(), dateTime);
    }
}
