package com.pao.proiect.audidealership.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class DatabaseConnection {
    private static final DatabaseConnection INSTANCE = new DatabaseConnection();
    private static final String PROPERTIES_FILE = "db.properties";

    private final Properties properties = new Properties();
    private Connection connection;

    private DatabaseConnection() {
        loadProperties();
    }

    public static DatabaseConnection getInstance() {
        return INSTANCE;
    }

    public synchronized Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                String url = properties.getProperty("db.url");
                loadDriverIfNeeded(url);
                String user = properties.getProperty("db.user");
                String password = properties.getProperty("db.password");
                if (user == null || user.isBlank()) {
                    connection = DriverManager.getConnection(url);
                } else {
                    connection = DriverManager.getConnection(url, user, password);
                }
            }
            return connection;
        } catch (SQLException e) {
            throw new IllegalStateException("Nu s-a putut deschide conexiunea la baza de date.", e);
        }
    }

    private void loadDriverIfNeeded(String url) {
        if (url == null || !url.startsWith("jdbc:sqlite")) {
            return;
        }

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ignored) {
            registerSqliteDriverFromLib();
        }
    }

    private void registerSqliteDriverFromLib() {
        Path libDirectory = Path.of("lib");
        try (Stream<Path> jars = Files.list(libDirectory)) {
            Path sqliteJar = jars
                    .filter(path -> path.getFileName().toString().startsWith("sqlite-jdbc"))
                    .filter(path -> path.getFileName().toString().endsWith(".jar"))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Nu exista sqlite-jdbc*.jar in folderul lib."));

            URLClassLoader loader = new URLClassLoader(new URL[]{sqliteJar.toUri().toURL()});
            Driver driver = (Driver) Class.forName("org.sqlite.JDBC", true, loader)
                    .getDeclaredConstructor()
                    .newInstance();
            DriverManager.registerDriver(new DriverShim(driver));
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Calea catre driverul SQLite este invalida.", e);
        } catch (ReflectiveOperationException | SQLException | IOException e) {
            throw new IllegalStateException("Nu s-a putut incarca driverul SQLite din lib.", e);
        }
    }

    private void loadProperties() {
        InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE);
        try (InputStream inputStream = resourceStream != null
                ? resourceStream
                : Files.newInputStream(Path.of("resources", PROPERTIES_FILE))) {
            if (inputStream == null) {
                throw new IllegalStateException("Fisierul resources/db.properties nu a fost gasit in classpath.");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new IllegalStateException("Nu s-a putut citi configuratia bazei de date.", e);
        }
    }

    private static class DriverShim implements Driver {
        private final Driver driver;

        DriverShim(Driver driver) {
            this.driver = driver;
        }

        @Override
        public Connection connect(String url, java.util.Properties info) throws SQLException {
            return driver.connect(url, info);
        }

        @Override
        public boolean acceptsURL(String url) throws SQLException {
            return driver.acceptsURL(url);
        }

        @Override
        public DriverPropertyInfo[] getPropertyInfo(String url, java.util.Properties info) throws SQLException {
            return driver.getPropertyInfo(url, info);
        }

        @Override
        public int getMajorVersion() {
            return driver.getMajorVersion();
        }

        @Override
        public int getMinorVersion() {
            return driver.getMinorVersion();
        }

        @Override
        public boolean jdbcCompliant() {
            return driver.jdbcCompliant();
        }

        @Override
        public Logger getParentLogger() {
            return Logger.getGlobal();
        }
    }
}
