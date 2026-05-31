package com.pao.proiect.audidealership.repository;

import com.pao.proiect.audidealership.model.AudiCar;
import com.pao.proiect.audidealership.model.Customer;
import com.pao.proiect.audidealership.model.Order;
import com.pao.proiect.audidealership.model.SalesAgent;
import com.pao.proiect.audidealership.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderRepository implements Repository<Order, String> {
    private static final OrderRepository INSTANCE = new OrderRepository();

    private OrderRepository() {
    }

    public static OrderRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public void save(Order order) {
        String sql = """
                INSERT INTO orders(id, customer_id, vehicle_vin, agent_id, created_at, completed, completed_at)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            bindOrder(statement, order);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Nu s-a putut salva comanda.", e);
        }
    }

    public void saveAndMarkVehicleSold(Order order) {
        Connection connection = connection();
        boolean previousAutoCommit;
        try {
            previousAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            insertOrder(connection, order);
            AudiCarRepository.getInstance().updateAvailability(connection, order.getCar().getVin().getValue(), false);
            connection.commit();
            connection.setAutoCommit(previousAutoCommit);
        } catch (SQLException e) {
            rollback(connection);
            throw new IllegalStateException("Tranzactia pentru vanzare a esuat.", e);
        }
    }

    @Override
    public Optional<Order> findById(String id) {
        String sql = joinSelect() + " WHERE o.id = ?";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapJoinedOrder(resultSet));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Nu s-a putut cauta comanda.", e);
        }
    }

    @Override
    public List<Order> findAll() {
        String sql = joinSelect() + " ORDER BY o.created_at DESC";
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement statement = connection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                orders.add(mapJoinedOrder(resultSet));
            }
            return orders;
        } catch (SQLException e) {
            throw new IllegalStateException("Nu s-au putut lista comenzile.", e);
        }
    }

    @Override
    public void update(Order order) {
        String sql = """
                UPDATE orders
                SET customer_id = ?, vehicle_vin = ?, agent_id = ?, created_at = ?, completed = ?, completed_at = ?
                WHERE id = ?
                """;
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setString(1, order.getCustomer().getId());
            statement.setString(2, order.getCar().getVin().getValue());
            statement.setString(3, order.getAgent().getId());
            statement.setTimestamp(4, Timestamp.valueOf(order.getCreatedAt()));
            statement.setBoolean(5, order.isCompleted());
            setNullableTimestamp(statement, 6, order.getCompletedAt());
            statement.setString(7, order.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Nu s-a putut actualiza comanda.", e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM orders WHERE id = ?";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Nu s-a putut sterge comanda.", e);
        }
    }

    public List<Order> findOrdersByCustomer(String customerId) {
        String sql = joinSelect() + " WHERE c.id = ? ORDER BY o.created_at DESC";
        return findJoinedOrders(sql, customerId);
    }

    public List<Order> findCompletedOrdersWithDetails() {
        String sql = joinSelect() + " WHERE o.completed = TRUE ORDER BY o.completed_at DESC";
        return findJoinedOrders(sql);
    }

    public List<String> countOrdersByAgent() {
        String sql = """
                SELECT sa.first_name, sa.last_name, COUNT(o.id) AS order_count
                FROM sales_agents sa
                LEFT JOIN orders o ON o.agent_id = sa.id
                GROUP BY sa.id, sa.first_name, sa.last_name
                ORDER BY order_count DESC
                """;
        List<String> results = new ArrayList<>();
        try (PreparedStatement statement = connection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                results.add(resultSet.getString("first_name") + " " + resultSet.getString("last_name")
                        + " - comenzi: " + resultSet.getInt("order_count"));
            }
            return results;
        } catch (SQLException e) {
            throw new IllegalStateException("Nu s-a putut calcula numarul de comenzi per agent.", e);
        }
    }

    private List<Order> findJoinedOrders(String sql, String... parameters) {
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            for (int i = 0; i < parameters.length; i++) {
                statement.setString(i + 1, parameters[i]);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    orders.add(mapJoinedOrder(resultSet));
                }
            }
            return orders;
        } catch (SQLException e) {
            throw new IllegalStateException("Nu s-au putut lista comenzile cu JOIN.", e);
        }
    }

    private void insertOrder(Connection connection, Order order) throws SQLException {
        String sql = """
                INSERT INTO orders(id, customer_id, vehicle_vin, agent_id, created_at, completed, completed_at)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            bindOrder(statement, order);
            statement.executeUpdate();
        }
    }

    private void bindOrder(PreparedStatement statement, Order order) throws SQLException {
        statement.setString(1, order.getId());
        statement.setString(2, order.getCustomer().getId());
        statement.setString(3, order.getCar().getVin().getValue());
        statement.setString(4, order.getAgent().getId());
        statement.setTimestamp(5, Timestamp.valueOf(order.getCreatedAt()));
        statement.setBoolean(6, order.isCompleted());
        setNullableTimestamp(statement, 7, order.getCompletedAt());
    }

    private static Order mapJoinedOrder(ResultSet resultSet) throws SQLException {
        Customer customer = new Customer(
                resultSet.getString("customer_id"),
                resultSet.getString("customer_first_name"),
                resultSet.getString("customer_last_name"),
                resultSet.getString("customer_email"),
                resultSet.getString("phone_number"),
                resultSet.getString("loyalty_tier")
        );
        AudiCar car = AudiCarRepository.mapCar(resultSet);
        SalesAgent agent = new SalesAgent(
                resultSet.getString("agent_id"),
                resultSet.getString("agent_first_name"),
                resultSet.getString("agent_last_name"),
                resultSet.getString("agent_email"),
                resultSet.getString("department"),
                resultSet.getDouble("salary"),
                resultSet.getString("region"),
                resultSet.getInt("years_of_experience")
        );
        Order order = new Order(
                resultSet.getString("order_id"),
                customer,
                car,
                agent,
                resultSet.getTimestamp("created_at").toLocalDateTime()
        );
        if (resultSet.getBoolean("completed")) {
            Timestamp completedAt = resultSet.getTimestamp("completed_at");
            order.complete(completedAt == null ? LocalDateTime.now() : completedAt.toLocalDateTime());
        }
        return order;
    }

    private static String joinSelect() {
        return """
                SELECT o.id AS order_id, o.created_at, o.completed, o.completed_at,
                       c.id AS customer_id, c.first_name AS customer_first_name, c.last_name AS customer_last_name,
                       c.email AS customer_email, c.phone_number, c.loyalty_tier,
                       v.vin, v.model_name, v.production_year, v.price, v.available, v.color,
                       v.body_style, v.fuel_type, v.horsepower, v.quattro_system,
                       v.battery_capacity_kwh, v.range_km, v.fast_charging,
                       sa.id AS agent_id, sa.first_name AS agent_first_name, sa.last_name AS agent_last_name,
                       sa.email AS agent_email, sa.department, sa.salary, sa.region, sa.years_of_experience
                FROM orders o
                JOIN customers c ON o.customer_id = c.id
                JOIN vehicles v ON o.vehicle_vin = v.vin
                JOIN sales_agents sa ON o.agent_id = sa.id
                """;
    }

    private void setNullableTimestamp(PreparedStatement statement, int index, LocalDateTime value) throws SQLException {
        if (value == null) {
            statement.setObject(index, null);
        } else {
            statement.setTimestamp(index, Timestamp.valueOf(value));
        }
    }

    private void rollback(Connection connection) {
        try {
            connection.rollback();
            connection.setAutoCommit(true);
        } catch (SQLException rollbackException) {
            throw new IllegalStateException("Rollback-ul tranzactiei a esuat.", rollbackException);
        }
    }

    private Connection connection() {
        return DatabaseConnection.getInstance().getConnection();
    }
}
