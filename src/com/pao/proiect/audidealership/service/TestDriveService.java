package com.pao.proiect.audidealership.service;

import com.pao.proiect.audidealership.model.AudiCar;
import com.pao.proiect.audidealership.model.Customer;
import com.pao.proiect.audidealership.model.TestDrive;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TestDriveService {
    private static final TestDriveService INSTANCE = new TestDriveService();

    private final List<TestDrive> testDrives = new ArrayList<>();
    private int testDriveCounter = 1;

    private TestDriveService() {
    }

    public static TestDriveService getInstance() {
        return INSTANCE;
    }

    public TestDrive scheduleTestDrive(Customer customer, AudiCar car, LocalDateTime dateTime) {
        if (Objects.isNull(customer) || Objects.isNull(car) || Objects.isNull(dateTime)) {
            throw new IllegalArgumentException("Clientul, masina si data sunt obligatorii.");
        }
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Test drive-ul nu poate fi programat in trecut.");
        }

        TestDrive testDrive = new TestDrive("TD-" + testDriveCounter++, customer, car, dateTime);
        testDrives.add(testDrive);
        return testDrive;
    }

    public List<TestDrive> listTestDrivesByCustomer(String customerId) {
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("ID-ul clientului nu poate fi gol.");
        }
        String normalizedId = customerId.trim();
        return testDrives.stream()
                .filter(testDrive -> testDrive.getCustomer().getId().equals(normalizedId))
                .toList();
    }

    public List<TestDrive> listAllTestDrives() {
        return new ArrayList<>(testDrives);
    }
}
