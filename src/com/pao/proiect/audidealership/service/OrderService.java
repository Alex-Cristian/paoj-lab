package com.pao.proiect.audidealership.service;

import com.pao.proiect.audidealership.exception.VehicleAlreadySoldException;
import com.pao.proiect.audidealership.exception.VehicleNotFoundException;
import com.pao.proiect.audidealership.model.AudiCar;
import com.pao.proiect.audidealership.model.Customer;
import com.pao.proiect.audidealership.model.Order;
import com.pao.proiect.audidealership.model.SalesAgent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OrderService {
    private static final OrderService INSTANCE = new OrderService();

    private final List<Order> orders = new ArrayList<>();
    private final Map<String, Order> ordersById = new HashMap<>();
    private final Map<String, List<Order>> ordersByCustomerId = new HashMap<>();
    private int orderCounter = 1;

    private OrderService() {
    }

    public static OrderService getInstance() {
        return INSTANCE;
    }

    public Order createOrder(Customer customer, AudiCar car, SalesAgent agent) {
        AuditService.getInstance().logAction("creeaza_comanda");
        validateOrderInput(customer, car, agent);
        if (!car.isAvailable()) {
            throw new VehicleAlreadySoldException("Masina cu VIN-ul " + car.getVin() + " nu mai este disponibila.");
        }

        String orderId = "ORD-" + orderCounter++;
        Order order = new Order(orderId, customer, car, agent, LocalDateTime.now());
        orders.add(order);
        ordersById.put(orderId, order);
        ordersByCustomerId.computeIfAbsent(customer.getId(), key -> new ArrayList<>()).add(order);
        return order;
    }

    public void completeOrder(String orderId) {
        AuditService.getInstance().logAction("finalizeaza_vanzare");
        Order order = findOrderById(orderId);
        if (order.isCompleted()) {
            throw new IllegalStateException("Comanda " + orderId + " este deja finalizata.");
        }

        VehicleService.getInstance().markVehicleAsSold(order.getCar().getVin().getValue());
        order.complete(LocalDateTime.now());
    }

    public List<Order> listOrdersByCustomer(String customerId) {
        AuditService.getInstance().logAction("listeaza_comenzi_client");
        validateText(customerId, "ID-ul clientului nu poate fi gol.");
        return new ArrayList<>(ordersByCustomerId.getOrDefault(customerId.trim(), List.of()));
    }

    public List<Order> listAllOrders() {
        AuditService.getInstance().logAction("listeaza_toate_comenzile");
        return new ArrayList<>(orders);
    }

    private Order findOrderById(String orderId) {
        validateText(orderId, "ID-ul comenzii nu poate fi gol.");
        Order order = ordersById.get(orderId.trim());
        if (order == null) {
            throw new VehicleNotFoundException("Comanda cu ID-ul " + orderId + " nu exista.");
        }
        return order;
    }

    private void validateOrderInput(Customer customer, AudiCar car, SalesAgent agent) {
        if (Objects.isNull(customer) || Objects.isNull(car) || Objects.isNull(agent)) {
            throw new IllegalArgumentException("Clientul, masina si agentul sunt obligatorii.");
        }
    }

    private void validateText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }
}
