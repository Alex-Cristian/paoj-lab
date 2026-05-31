package com.pao.proiect.audidealership.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SchemaInitializer {
    private SchemaInitializer() {
    }

    public static void initializeSchema() {
        Path schemaPath = Path.of("resources", "schema.sql");
        try {
            String schema = Files.readString(schemaPath);
            Connection connection = DatabaseConnection.getInstance().getConnection();
            try (Statement statement = connection.createStatement()) {
                for (String sql : schema.split(";")) {
                    String normalizedSql = sql.trim();
                    if (!normalizedSql.isBlank() && !normalizedSql.toUpperCase().startsWith("DROP TABLE")) {
                        statement.execute(normalizedSql.replace("CREATE TABLE ", "CREATE TABLE IF NOT EXISTS "));
                    }
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Nu s-a putut citi resources/schema.sql.", e);
        } catch (SQLException e) {
            throw new IllegalStateException("Nu s-a putut initializa schema bazei de date.", e);
        }
    }
}
