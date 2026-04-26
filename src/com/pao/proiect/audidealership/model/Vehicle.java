package com.pao.proiect.audidealership.model;

public abstract class Vehicle implements Identifiable {
    private final VIN vin;
    private String modelName;
    private int productionYear;
    private double price;
    private boolean available;
    private String color;

    protected Vehicle(VIN vin, String modelName, int productionYear, double price, String color) {
        this.vin = requireVin(vin);
        this.modelName = requireText(modelName, "Modelul nu poate fi gol.");
        setProductionYear(productionYear);
        setPrice(price);
        this.color = requireText(color, "Culoarea nu poate fi goala.");
        this.available = true;
    }

    protected final String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }

    protected final VIN requireVin(VIN value) {
        if (value == null) {
            throw new IllegalArgumentException("VIN-ul nu poate fi null.");
        }
        return value;
    }

    @Override
    public String getId() {
        return vin.getValue();
    }

    public VIN getVin() {
        return vin;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = requireText(modelName, "Modelul nu poate fi gol.");
    }

    public int getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(int productionYear) {
        if (productionYear < 2000 || productionYear > 2100) {
            throw new IllegalArgumentException("Anul de productie este invalid.");
        }
        this.productionYear = productionYear;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Pretul trebuie sa fie pozitiv.");
        }
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = requireText(color, "Culoarea nu poate fi goala.");
    }

    public abstract String getVehicleType();
}
