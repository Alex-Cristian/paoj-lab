package com.pao.proiect.audidealership.model;

import java.time.LocalDateTime;

public class Order implements Identifiable {
    private final String id;
    private final Customer customer;
    private final AudiCar car;
    private final SalesAgent agent;
    private final LocalDateTime createdAt;
    private boolean completed;
    private LocalDateTime completedAt;

    public Order(String id, Customer customer, AudiCar car, SalesAgent agent, LocalDateTime createdAt) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID-ul comenzii nu poate fi gol.");
        }
        if (customer == null || car == null || agent == null || createdAt == null) {
            throw new IllegalArgumentException("Datele comenzii nu pot fi null.");
        }
        this.id = id.trim();
        this.customer = customer;
        this.car = car;
        this.agent = agent;
        this.createdAt = createdAt;
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

    public SalesAgent getAgent() {
        return agent;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isCompleted() {
        return completed;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void complete(LocalDateTime completionTime) {
        if (completionTime == null) {
            throw new IllegalArgumentException("Data de finalizare nu poate fi null.");
        }
        this.completed = true;
        this.completedAt = completionTime;
    }

    @Override
    public String toString() {
        return "Order{id='%s', customer='%s', vin='%s', agent='%s', completed=%s}"
                .formatted(id, customer.getFullName(), car.getVin(), agent.getFullName(), completed);
    }
}
