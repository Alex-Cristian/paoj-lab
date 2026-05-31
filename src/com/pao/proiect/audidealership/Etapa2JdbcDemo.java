package com.pao.proiect.audidealership;

import com.pao.proiect.audidealership.model.AudiCar;
import com.pao.proiect.audidealership.model.Customer;
import com.pao.proiect.audidealership.model.Order;
import com.pao.proiect.audidealership.model.SalesAgent;
import com.pao.proiect.audidealership.model.VIN;
import com.pao.proiect.audidealership.repository.AudiCarRepository;
import com.pao.proiect.audidealership.repository.CustomerRepository;
import com.pao.proiect.audidealership.repository.OrderRepository;
import com.pao.proiect.audidealership.repository.SalesAgentRepository;
import com.pao.proiect.audidealership.service.AuditService;
import com.pao.proiect.audidealership.util.SchemaInitializer;

import java.time.LocalDateTime;

public class Etapa2JdbcDemo {
    private static final String CUSTOMER_ID = "ET2-C1";
    private static final String CAR_VIN = "WAUZZZ8K1DA222222";
    private static final String AGENT_ID = "ET2-A1";
    private static final String ORDER_ID = "ET2-O1";

    public static void main(String[] args) {
        try {
            SchemaInitializer.initializeSchema();

            CustomerRepository customerRepository = CustomerRepository.getInstance();
            AudiCarRepository carRepository = AudiCarRepository.getInstance();
            SalesAgentRepository agentRepository = SalesAgentRepository.getInstance();
            OrderRepository orderRepository = OrderRepository.getInstance();

            if (args.length > 0 && "read".equalsIgnoreCase(args[0])) {
                System.out.println("Verific datele salvate deja in baza de date.");
                printDatabaseState(customerRepository, carRepository, orderRepository);
                return;
            }

            Customer customer = new Customer(CUSTOMER_ID, "Ioana", "Dumitrescu",
                    "ioana.dumitrescu@mail.com", "0722000001", "Platinum");
            AudiCar car = new AudiCar(new VIN(CAR_VIN), "Audi A6", 2024,
                    68990, "Mythos Black", "Sedan", "Benzina", 340, "quattro");
            SalesAgent agent = new SalesAgent(AGENT_ID, "Andrei", "Stan",
                    "andrei.stan@audi.ro", "Sales", 7200, "Bucuresti", 5);
            Order order = new Order(ORDER_ID, customer, car, agent, LocalDateTime.now());
            order.complete(LocalDateTime.now());

            cleanup(orderRepository, carRepository, customerRepository, agentRepository);

            customerRepository.save(customer);
            carRepository.save(car);
            agentRepository.save(agent);
            orderRepository.saveAndMarkVehicleSold(order);
            AuditService.getInstance().logAction("demo_etapa2_jdbc");

            System.out.println("Am scris datele in baza de date.");
            printDatabaseState(customerRepository, carRepository, orderRepository);
        } catch (IllegalStateException e) {
            System.out.println("Demo-ul JDBC nu poate porni inca: " + e.getMessage());
            Throwable cause = e.getCause();
            if (cause != null) {
                System.out.println("Cauza tehnica: " + cause.getMessage());
            }
            System.out.println("Verifica baza de date, driverul JDBC si resources/db.properties.");
        }
    }

    private static void cleanup(OrderRepository orderRepository,
                                AudiCarRepository carRepository,
                                CustomerRepository customerRepository,
                                SalesAgentRepository agentRepository) {
        orderRepository.delete(ORDER_ID);
        carRepository.delete(CAR_VIN);
        customerRepository.delete(CUSTOMER_ID);
        agentRepository.delete(AGENT_ID);
    }

    private static void printDatabaseState(CustomerRepository customerRepository,
                                           AudiCarRepository carRepository,
                                           OrderRepository orderRepository) {
        System.out.println("Clienti din DB:");
        customerRepository.findAll().forEach(System.out::println);

        System.out.println("\nMasini din DB:");
        carRepository.findAll().forEach(System.out::println);

        System.out.println("\nComenzi cu JOIN client + masina + agent:");
        orderRepository.findAll().forEach(System.out::println);

        System.out.println("\nComenzile clientului " + CUSTOMER_ID + ":");
        orderRepository.findOrdersByCustomer(CUSTOMER_ID).forEach(System.out::println);

        System.out.println("\nComenzi finalizate:");
        orderRepository.findCompletedOrdersWithDetails().forEach(System.out::println);

        System.out.println("\nNumar comenzi per agent:");
        orderRepository.countOrdersByAgent().forEach(System.out::println);
    }
}
