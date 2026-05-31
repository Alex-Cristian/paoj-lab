package com.pao.proiect.audidealership.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

public class AuditService {
    private static final Path AUDIT_FILE = Path.of("audit.csv");
    private static final AuditService INSTANCE = new AuditService();

    private AuditService() {
        ensureHeader();
    }

    public static AuditService getInstance() {
        return INSTANCE;
    }

    public synchronized void logAction(String actionName) {
        if (actionName == null || actionName.isBlank()) {
            throw new IllegalArgumentException("Numele actiunii nu poate fi gol.");
        }

        try (BufferedWriter writer = Files.newBufferedWriter(
                AUDIT_FILE,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        )) {
            writer.write(actionName.trim() + "," + LocalDateTime.now());
            writer.newLine();
        } catch (IOException e) {
            throw new IllegalStateException("Nu s-a putut scrie in fisierul de audit.", e);
        }
    }

    private synchronized void ensureHeader() {
        try {
            if (Files.notExists(AUDIT_FILE) || Files.size(AUDIT_FILE) == 0) {
                try (BufferedWriter writer = Files.newBufferedWriter(
                        AUDIT_FILE,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND
                )) {
                    writer.write("nume_actiune,timestamp");
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Nu s-a putut initializa fisierul de audit.", e);
        }
    }
}
