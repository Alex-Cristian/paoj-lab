package com.pao.proiect.audidealership.repository;

import com.pao.proiect.audidealership.model.SalesAgent;
import com.pao.proiect.audidealership.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SalesAgentRepository implements Repository<SalesAgent, String> {
    private static final SalesAgentRepository INSTANCE = new SalesAgentRepository();

    private SalesAgentRepository() {
    }

    public static SalesAgentRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public void save(SalesAgent agent) {
        String sql = """
                INSERT INTO sales_agents(id, first_name, last_name, email, department, salary, region, years_of_experience)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            bindAgent(statement, agent);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Nu s-a putut salva agentul.", e);
        }
    }

    @Override
    public Optional<SalesAgent> findById(String id) {
        String sql = "SELECT * FROM sales_agents WHERE id = ?";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapAgent(resultSet));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Nu s-a putut cauta agentul.", e);
        }
    }

    @Override
    public List<SalesAgent> findAll() {
        String sql = "SELECT * FROM sales_agents ORDER BY last_name, first_name";
        List<SalesAgent> agents = new ArrayList<>();
        try (PreparedStatement statement = connection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                agents.add(mapAgent(resultSet));
            }
            return agents;
        } catch (SQLException e) {
            throw new IllegalStateException("Nu s-au putut lista agentii.", e);
        }
    }

    @Override
    public void update(SalesAgent agent) {
        String sql = """
                UPDATE sales_agents
                SET first_name = ?, last_name = ?, email = ?, department = ?, salary = ?, region = ?, years_of_experience = ?
                WHERE id = ?
                """;
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setString(1, agent.getFirstName());
            statement.setString(2, agent.getLastName());
            statement.setString(3, agent.getEmail());
            statement.setString(4, agent.getDepartment());
            statement.setDouble(5, agent.getSalary());
            statement.setString(6, agent.getRegion());
            statement.setInt(7, agent.getYearsOfExperience());
            statement.setString(8, agent.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Nu s-a putut actualiza agentul.", e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM sales_agents WHERE id = ?";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Nu s-a putut sterge agentul.", e);
        }
    }

    private void bindAgent(PreparedStatement statement, SalesAgent agent) throws SQLException {
        statement.setString(1, agent.getId());
        statement.setString(2, agent.getFirstName());
        statement.setString(3, agent.getLastName());
        statement.setString(4, agent.getEmail());
        statement.setString(5, agent.getDepartment());
        statement.setDouble(6, agent.getSalary());
        statement.setString(7, agent.getRegion());
        statement.setInt(8, agent.getYearsOfExperience());
    }

    static SalesAgent mapAgent(ResultSet resultSet) throws SQLException {
        return new SalesAgent(
                resultSet.getString("id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getString("email"),
                resultSet.getString("department"),
                resultSet.getDouble("salary"),
                resultSet.getString("region"),
                resultSet.getInt("years_of_experience")
        );
    }

    private Connection connection() {
        return DatabaseConnection.getInstance().getConnection();
    }
}
