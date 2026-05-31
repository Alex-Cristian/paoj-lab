package com.pao.proiect.audidealership.repository;

import com.pao.proiect.audidealership.model.Customer;
import com.pao.proiect.audidealership.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerRepository implements Repository<Customer, String> {
    private static final CustomerRepository INSTANCE = new CustomerRepository();

    private CustomerRepository() {
    }

    public static CustomerRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public void save(Customer customer) {
        String sql = """
                INSERT INTO customers(id, first_name, last_name, email, phone_number, loyalty_tier)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            bindCustomer(statement, customer);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Nu s-a putut salva clientul.", e);
        }
    }

    @Override
    public Optional<Customer> findById(String id) {
        String sql = "SELECT * FROM customers WHERE id = ?";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapCustomer(resultSet));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Nu s-a putut cauta clientul.", e);
        }
    }

    @Override
    public List<Customer> findAll() {
        String sql = "SELECT * FROM customers ORDER BY last_name, first_name";
        List<Customer> customers = new ArrayList<>();
        try (PreparedStatement statement = connection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                customers.add(mapCustomer(resultSet));
            }
            return customers;
        } catch (SQLException e) {
            throw new IllegalStateException("Nu s-au putut lista clientii.", e);
        }
    }

    @Override
    public void update(Customer customer) {
        String sql = """
                UPDATE customers
                SET first_name = ?, last_name = ?, email = ?, phone_number = ?, loyalty_tier = ?
                WHERE id = ?
                """;
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getPhoneNumber());
            statement.setString(5, customer.getLoyaltyTier());
            statement.setString(6, customer.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Nu s-a putut actualiza clientul.", e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM customers WHERE id = ?";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Nu s-a putut sterge clientul.", e);
        }
    }

    private void bindCustomer(PreparedStatement statement, Customer customer) throws SQLException {
        statement.setString(1, customer.getId());
        statement.setString(2, customer.getFirstName());
        statement.setString(3, customer.getLastName());
        statement.setString(4, customer.getEmail());
        statement.setString(5, customer.getPhoneNumber());
        statement.setString(6, customer.getLoyaltyTier());
    }

    static Customer mapCustomer(ResultSet resultSet) throws SQLException {
        return new Customer(
                resultSet.getString("id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getString("email"),
                resultSet.getString("phone_number"),
                resultSet.getString("loyalty_tier")
        );
    }

    private Connection connection() {
        return DatabaseConnection.getInstance().getConnection();
    }
}
