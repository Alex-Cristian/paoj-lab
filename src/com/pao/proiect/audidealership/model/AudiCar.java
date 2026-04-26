package com.pao.proiect.audidealership.model;

import java.util.Objects;

public class AudiCar extends Vehicle {
    private String bodyStyle;
    private String fuelType;
    private int horsepower;
    private String quattroSystem;

    public AudiCar(VIN vin, String modelName, int productionYear, double price, String color,
                   String bodyStyle, String fuelType, int horsepower, String quattroSystem) {
        super(vin, modelName, productionYear, price, color);
        this.bodyStyle = requireText(bodyStyle, "Caroseria nu poate fi goala.");
        this.fuelType = requireText(fuelType, "Tipul de combustibil nu poate fi gol.");
        setHorsepower(horsepower);
        this.quattroSystem = requireText(quattroSystem, "Configuratia quattro nu poate fi goala.");
    }

    public String getBodyStyle() {
        return bodyStyle;
    }

    public void setBodyStyle(String bodyStyle) {
        this.bodyStyle = requireText(bodyStyle, "Caroseria nu poate fi goala.");
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = requireText(fuelType, "Tipul de combustibil nu poate fi gol.");
    }

    public int getHorsepower() {
        return horsepower;
    }

    public void setHorsepower(int horsepower) {
        if (horsepower <= 0) {
            throw new IllegalArgumentException("Puterea trebuie sa fie pozitiva.");
        }
        this.horsepower = horsepower;
    }

    public String getQuattroSystem() {
        return quattroSystem;
    }

    public void setQuattroSystem(String quattroSystem) {
        this.quattroSystem = requireText(quattroSystem, "Configuratia quattro nu poate fi goala.");
    }

    @Override
    public String getVehicleType() {
        return "Audi";
    }

    @Override
    public String toString() {
        return "AudiCar{vin='%s', model='%s', year=%d, price=%.2f, available=%s, color='%s', fuel='%s', hp=%d}"
                .formatted(getVin(), getModelName(), getProductionYear(), getPrice(), isAvailable(),
                        getColor(), fuelType, horsepower);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AudiCar audiCar)) {
            return false;
        }
        return Objects.equals(getVin(), audiCar.getVin());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVin());
    }
}
