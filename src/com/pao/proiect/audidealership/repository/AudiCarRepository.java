package com.pao.proiect.audidealership.repository;

import com.pao.proiect.audidealership.model.AudiCar;
import com.pao.proiect.audidealership.model.ElectricAudiCar;
import com.pao.proiect.audidealership.model.VIN;
import com.pao.proiect.audidealership.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AudiCarRepository implements Repository<AudiCar, String> {
    private static final AudiCarRepository INSTANCE = new AudiCarRepository();

    private AudiCarRepository() {
    }

    public static AudiCarRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public void save(AudiCar car) {
        String sql = """
                INSERT INTO vehicles(vin, model_name, production_year, price, available, color,
                                     body_style, fuel_type, horsepower, quattro_system,
                                     battery_capacity_kwh, range_km, fast_charging)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            bindCar(statement, car);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Nu s-a putut salva masina.", e);
        }
    }

    @Override
    public Optional<AudiCar> findById(String vin) {
        String sql = "SELECT * FROM vehicles WHERE vin = ?";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setString(1, vin);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapCar(resultSet));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Nu s-a putut cauta masina.", e);
        }
    }

    @Override
    public List<AudiCar> findAll() {
        String sql = "SELECT * FROM vehicles ORDER BY price";
        List<AudiCar> cars = new ArrayList<>();
        try (PreparedStatement statement = connection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                cars.add(mapCar(resultSet));
            }
            return cars;
        } catch (SQLException e) {
            throw new IllegalStateException("Nu s-au putut lista masinile.", e);
        }
    }

    @Override
    public void update(AudiCar car) {
        String sql = """
                UPDATE vehicles
                SET model_name = ?, production_year = ?, price = ?, available = ?, color = ?,
                    body_style = ?, fuel_type = ?, horsepower = ?, quattro_system = ?,
                    battery_capacity_kwh = ?, range_km = ?, fast_charging = ?
                WHERE vin = ?
                """;
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setString(1, car.getModelName());
            statement.setInt(2, car.getProductionYear());
            statement.setDouble(3, car.getPrice());
            statement.setBoolean(4, car.isAvailable());
            statement.setString(5, car.getColor());
            statement.setString(6, car.getBodyStyle());
            statement.setString(7, car.getFuelType());
            statement.setInt(8, car.getHorsepower());
            statement.setString(9, car.getQuattroSystem());
            bindElectricFields(statement, car, 10);
            statement.setString(13, car.getVin().getValue());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Nu s-a putut actualiza masina.", e);
        }
    }

    @Override
    public void delete(String vin) {
        String sql = "DELETE FROM vehicles WHERE vin = ?";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setString(1, vin);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Nu s-a putut sterge masina.", e);
        }
    }

    public void updateAvailability(Connection connection, String vin, boolean available) throws SQLException {
        String sql = "UPDATE vehicles SET available = ? WHERE vin = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, available);
            statement.setString(2, vin);
            statement.executeUpdate();
        }
    }

    private void bindCar(PreparedStatement statement, AudiCar car) throws SQLException {
        statement.setString(1, car.getVin().getValue());
        statement.setString(2, car.getModelName());
        statement.setInt(3, car.getProductionYear());
        statement.setDouble(4, car.getPrice());
        statement.setBoolean(5, car.isAvailable());
        statement.setString(6, car.getColor());
        statement.setString(7, car.getBodyStyle());
        statement.setString(8, car.getFuelType());
        statement.setInt(9, car.getHorsepower());
        statement.setString(10, car.getQuattroSystem());
        bindElectricFields(statement, car, 11);
    }

    private void bindElectricFields(PreparedStatement statement, AudiCar car, int startIndex) throws SQLException {
        if (car instanceof ElectricAudiCar electricCar) {
            statement.setInt(startIndex, electricCar.getBatteryCapacityKwh());
            statement.setInt(startIndex + 1, electricCar.getRangeKm());
            statement.setBoolean(startIndex + 2, electricCar.isFastCharging());
        } else {
            statement.setObject(startIndex, null);
            statement.setObject(startIndex + 1, null);
            statement.setObject(startIndex + 2, null);
        }
    }

    static AudiCar mapCar(ResultSet resultSet) throws SQLException {
        Number battery = (Number) resultSet.getObject("battery_capacity_kwh");
        Number range = (Number) resultSet.getObject("range_km");
        AudiCar car;
        if (battery != null && range != null) {
            car = new ElectricAudiCar(
                    new VIN(resultSet.getString("vin")),
                    resultSet.getString("model_name"),
                    resultSet.getInt("production_year"),
                    resultSet.getDouble("price"),
                    resultSet.getString("color"),
                    resultSet.getString("body_style"),
                    resultSet.getInt("horsepower"),
                    resultSet.getString("quattro_system"),
                    battery.intValue(),
                    range.intValue(),
                    resultSet.getBoolean("fast_charging")
            );
        } else {
            car = new AudiCar(
                    new VIN(resultSet.getString("vin")),
                    resultSet.getString("model_name"),
                    resultSet.getInt("production_year"),
                    resultSet.getDouble("price"),
                    resultSet.getString("color"),
                    resultSet.getString("body_style"),
                    resultSet.getString("fuel_type"),
                    resultSet.getInt("horsepower"),
                    resultSet.getString("quattro_system")
            );
        }
        car.setAvailable(resultSet.getBoolean("available"));
        return car;
    }

    private Connection connection() {
        return DatabaseConnection.getInstance().getConnection();
    }
}
