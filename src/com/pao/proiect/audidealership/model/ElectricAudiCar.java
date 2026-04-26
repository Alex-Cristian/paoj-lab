package com.pao.proiect.audidealership.model;

public class ElectricAudiCar extends AudiCar {
    private int batteryCapacityKwh;
    private int rangeKm;
    private boolean fastCharging;

    public ElectricAudiCar(VIN vin, String modelName, int productionYear, double price, String color,
                           String bodyStyle, int horsepower, String quattroSystem,
                           int batteryCapacityKwh, int rangeKm, boolean fastCharging) {
        super(vin, modelName, productionYear, price, color, bodyStyle, "Electric", horsepower, quattroSystem);
        setBatteryCapacityKwh(batteryCapacityKwh);
        setRangeKm(rangeKm);
        this.fastCharging = fastCharging;
    }

    public int getBatteryCapacityKwh() {
        return batteryCapacityKwh;
    }

    public void setBatteryCapacityKwh(int batteryCapacityKwh) {
        if (batteryCapacityKwh <= 0) {
            throw new IllegalArgumentException("Capacitatea bateriei trebuie sa fie pozitiva.");
        }
        this.batteryCapacityKwh = batteryCapacityKwh;
    }

    public int getRangeKm() {
        return rangeKm;
    }

    public void setRangeKm(int rangeKm) {
        if (rangeKm <= 0) {
            throw new IllegalArgumentException("Autonomia trebuie sa fie pozitiva.");
        }
        this.rangeKm = rangeKm;
    }

    public boolean isFastCharging() {
        return fastCharging;
    }

    public void setFastCharging(boolean fastCharging) {
        this.fastCharging = fastCharging;
    }

    @Override
    public String getVehicleType() {
        return "Electric Audi";
    }

    @Override
    public String toString() {
        return "ElectricAudiCar{vin='%s', model='%s', price=%.2f, rangeKm=%d, battery=%dkWh, available=%s}"
                .formatted(getVin(), getModelName(), getPrice(), rangeKm, batteryCapacityKwh, isAvailable());
    }
}
