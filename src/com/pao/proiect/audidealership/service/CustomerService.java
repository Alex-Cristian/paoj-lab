package com.pao.proiect.audidealership.service;

import com.pao.proiect.audidealership.exception.CustomerNotFoundException;
import com.pao.proiect.audidealership.model.Customer;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public class CustomerService {
    private static final CustomerService INSTANCE = new CustomerService();

    private final Set<Customer> customers = new LinkedHashSet<>();

    private CustomerService() {
    }

    public static CustomerService getInstance() {
        return INSTANCE;
    }

    public void addCustomer(Customer customer) {
        AuditService.getInstance().logAction("inregistreaza_client");
        validateCustomer(customer);
        if (!customers.add(customer)) {
            throw new IllegalArgumentException("Clientul cu ID-ul " + customer.getId() + " exista deja.");
        }
    }

    public void removeCustomerById(String id) {
        AuditService.getInstance().logAction("sterge_client");
        Customer customer = findCustomerById(id);
        customers.remove(customer);
    }

    public Customer findCustomerById(String id) {
        AuditService.getInstance().logAction("cauta_client_dupa_id");
        return customers.stream()
                .filter(customer -> customer.getId().equals(normalizeId(id)))
                .findFirst()
                .orElseThrow(() -> new CustomerNotFoundException("Clientul cu ID-ul " + id + " nu exista."));
    }

    public List<Customer> findCustomersByName(String name) {
        AuditService.getInstance().logAction("cauta_clienti_dupa_nume");
        String query = normalizeName(name);
        return customers.stream()
                .filter(customer -> customer.getFullName().toLowerCase(Locale.ROOT).contains(query))
                .toList();
    }

    public List<Customer> listAllCustomers() {
        AuditService.getInstance().logAction("listeaza_toti_clientii");
        return new ArrayList<>(customers);
    }

    private void validateCustomer(Customer customer) {
        if (Objects.isNull(customer)) {
            throw new IllegalArgumentException("Clientul nu poate fi null.");
        }
    }

    private String normalizeId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID-ul nu poate fi gol.");
        }
        return id.trim();
    }

    private String normalizeName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Numele cautat nu poate fi gol.");
        }
        return name.trim().toLowerCase(Locale.ROOT);
    }
}
