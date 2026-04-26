package com.pao.proiect.audidealership.model;

public class Showroom implements Identifiable {
    private final String id;
    private String name;
    private String city;
    private int displayCapacity;

    public Showroom(String id, String name, String city, int displayCapacity) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID-ul showroom-ului nu poate fi gol.");
        }
        this.id = id.trim();
        setName(name);
        setCity(city);
        setDisplayCapacity(displayCapacity);
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Numele showroom-ului nu poate fi gol.");
        }
        this.name = name.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("Orasul showroom-ului nu poate fi gol.");
        }
        this.city = city.trim();
    }

    public int getDisplayCapacity() {
        return displayCapacity;
    }

    public void setDisplayCapacity(int displayCapacity) {
        if (displayCapacity <= 0) {
            throw new IllegalArgumentException("Capacitatea trebuie sa fie pozitiva.");
        }
        this.displayCapacity = displayCapacity;
    }

    @Override
    public String toString() {
        return "Showroom{id='%s', name='%s', city='%s', capacity=%d}"
                .formatted(id, name, city, displayCapacity);
    }
}
