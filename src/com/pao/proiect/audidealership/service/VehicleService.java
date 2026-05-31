package com.pao.proiect.audidealership.service;

import com.pao.proiect.audidealership.exception.VehicleAlreadySoldException;
import com.pao.proiect.audidealership.exception.VehicleNotFoundException;
import com.pao.proiect.audidealership.model.AudiCar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class VehicleService {
    private static final VehicleService INSTANCE = new VehicleService();

    private final List<AudiCar> inventory = new ArrayList<>();
    private final Map<String, AudiCar> vehiclesByVin = new HashMap<>();

    private VehicleService() {
    }

    public static VehicleService getInstance() {
        return INSTANCE;
    }

    public void addVehicle(AudiCar car) {
        AuditService.getInstance().logAction("adauga_masina");
        validateCar(car);
        String vin = car.getVin().getValue();
        if (vehiclesByVin.containsKey(vin)) {
            throw new IllegalArgumentException("Exista deja o masina cu VIN-ul " + vin + ".");
        }
        inventory.add(car);
        vehiclesByVin.put(vin, car);
    }

    public void removeVehicleByVin(String vin) {
        AuditService.getInstance().logAction("sterge_masina");
        AudiCar car = findVehicleByVin(vin);
        inventory.remove(car);
        vehiclesByVin.remove(normalizeVin(vin));
    }

    public AudiCar findVehicleByVin(String vin) {
        AuditService.getInstance().logAction("cauta_masina_dupa_vin");
        AudiCar car = vehiclesByVin.get(normalizeVin(vin));
        if (car == null) {
            throw new VehicleNotFoundException("Nu exista nicio masina cu VIN-ul " + vin + ".");
        }
        return car;
    }

    public List<AudiCar> listAllVehicles() {
        AuditService.getInstance().logAction("listeaza_toate_masinile");
        return new ArrayList<>(inventory);
    }

    public List<AudiCar> listAvailableVehicles() {
        AuditService.getInstance().logAction("listeaza_masini_disponibile");
        return inventory.stream()
                .filter(AudiCar::isAvailable)
                .toList();
    }

    public List<AudiCar> listVehiclesSortedByPrice() {
        AuditService.getInstance().logAction("listeaza_masini_sortate_dupa_pret");
        return inventory.stream()
                .sorted(Comparator.comparingDouble(AudiCar::getPrice))
                .toList();
    }

    public void markVehicleAsSold(String vin) {
        AuditService.getInstance().logAction("marcheaza_masina_vanduta");
        AudiCar car = findVehicleByVin(vin);
        if (!car.isAvailable()) {
            throw new VehicleAlreadySoldException("Masina cu VIN-ul " + vin + " este deja vanduta.");
        }
        car.setAvailable(false);
    }

    private void validateCar(AudiCar car) {
        if (Objects.isNull(car)) {
            throw new IllegalArgumentException("Masina nu poate fi null.");
        }
    }

    private String normalizeVin(String vin) {
        if (vin == null || vin.isBlank()) {
            throw new IllegalArgumentException("VIN-ul cautat nu poate fi gol.");
        }
        return vin.trim().toUpperCase();
    }
}
