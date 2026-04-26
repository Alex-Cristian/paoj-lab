package com.pao.proiect.audidealership;

import com.pao.proiect.audidealership.exception.CustomerNotFoundException;
import com.pao.proiect.audidealership.exception.InvalidVinException;
import com.pao.proiect.audidealership.exception.VehicleAlreadySoldException;
import com.pao.proiect.audidealership.exception.VehicleNotFoundException;
import com.pao.proiect.audidealership.model.AudiCar;
import com.pao.proiect.audidealership.model.Customer;
import com.pao.proiect.audidealership.model.ElectricAudiCar;
import com.pao.proiect.audidealership.model.Order;
import com.pao.proiect.audidealership.model.SalesAgent;
import com.pao.proiect.audidealership.model.Showroom;
import com.pao.proiect.audidealership.model.TestDrive;
import com.pao.proiect.audidealership.model.VIN;
import com.pao.proiect.audidealership.service.CustomerService;
import com.pao.proiect.audidealership.service.OrderService;
import com.pao.proiect.audidealership.service.TestDriveService;
import com.pao.proiect.audidealership.service.VehicleService;

import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        VehicleService vehicleService = VehicleService.getInstance();
        CustomerService customerService = CustomerService.getInstance();
        OrderService orderService = OrderService.getInstance();
        TestDriveService testDriveService = TestDriveService.getInstance();

        Showroom showroom = new Showroom("SH-01", "Audi Center Bucuresti", "Bucuresti", 30);
        System.out.println("Showroom activ: " + showroom);

        SalesAgent salesAgent = new SalesAgent(
                "AG-01", "Mihai", "Popescu", "mihai.popescu@audi.ro",
                "Sales", 6800, "Bucuresti-Ilfov", 6
        );

        AudiCar audiA4 = new AudiCar(
                new VIN("WAUZZZ8K1DA123456"), "Audi A4", 2023, 38990, "Daytona Gray",
                "Sedan", "Benzina", 204, "quattro ultra"
        );
        AudiCar audiQ5 = new AudiCar(
                new VIN("WAUZZZFY1PA654321"), "Audi Q5", 2024, 54990, "Glacier White",
                "SUV", "Diesel", 265, "quattro"
        );
        ElectricAudiCar audiQ4Etron = new ElectricAudiCar(
                new VIN("WAUZZZFZ7RA112233"), "Audi Q4 e-tron", 2024, 61990, "Pebble Gray",
                "SUV", 286, "electric quattro", 82, 520, true
        );

        Customer customer1 = new Customer("C-100", "Ana", "Ionescu", "ana.ionescu@mail.com", "0711000001", "Gold");
        Customer customer2 = new Customer("C-101", "Radu", "Marin", "radu.marin@mail.com", "0711000002", "Silver");

        try {
            System.out.println("\n1. Adaug masini in inventar");
            vehicleService.addVehicle(audiA4);
            vehicleService.addVehicle(audiQ5);
            vehicleService.addVehicle(audiQ4Etron);
            printList(vehicleService.listAllVehicles());

            System.out.println("\n2. Inregistrez clienti");
            customerService.addCustomer(customer1);
            customerService.addCustomer(customer2);
            printList(customerService.listAllCustomers());

            System.out.println("\n3. Caut masina dupa VIN");
            System.out.println(vehicleService.findVehicleByVin("WAUZZZ8K1DA123456"));

            System.out.println("\n4. Listez toate masinile disponibile");
            printList(vehicleService.listAvailableVehicles());

            System.out.println("\n5. Listez masinile sortate dupa pret");
            printList(vehicleService.listVehiclesSortedByPrice());

            System.out.println("\n6. Caut clienti dupa nume");
            printList(customerService.findCustomersByName("ana"));

            System.out.println("\n7. Creez o comanda pentru client");
            Order firstOrder = orderService.createOrder(customer1, audiQ5, salesAgent);
            System.out.println(firstOrder);

            System.out.println("\n8. Finalizez vanzarea");
            orderService.completeOrder(firstOrder.getId());
            printList(orderService.listAllOrders());

            System.out.println("\n9. Programez test drive");
            TestDrive testDrive1 = testDriveService.scheduleTestDrive(
                    customer2, audiQ4Etron, LocalDateTime.now().plusDays(2).withHour(11).withMinute(0).withSecond(0).withNano(0)
            );
            TestDrive testDrive2 = testDriveService.scheduleTestDrive(
                    customer1, audiA4, LocalDateTime.now().plusDays(3).withHour(15).withMinute(30).withSecond(0).withNano(0)
            );
            System.out.println(testDrive1);
            System.out.println(testDrive2);

            System.out.println("\n10. Listez comenzile clientului");
            printList(orderService.listOrdersByCustomer(customer1.getId()));

            System.out.println("\n11. Listez toate test drive-urile");
            printList(testDriveService.listAllTestDrives());

            System.out.println("\n12. Listez test drive-urile unui client");
            printList(testDriveService.listTestDrivesByCustomer(customer1.getId()));

            System.out.println("\n13. Sterg o masina dupa VIN");
            vehicleService.removeVehicleByVin("WAUZZZ8K1DA123456");
            printList(vehicleService.listAllVehicles());

            System.out.println("\n14. Sterg un client dupa ID");
            customerService.removeCustomerById(customer2.getId());
            printList(customerService.listAllCustomers());

            System.out.println("\n15. Demonstrez tratarea exceptiilor custom");
            try {
                vehicleService.findVehicleByVin("INVALIDVIN1234567");
            } catch (VehicleNotFoundException e) {
                System.out.println("Exceptie capturata: " + e.getMessage());
            }

            try {
                customerService.findCustomerById("C-404");
            } catch (CustomerNotFoundException e) {
                System.out.println("Exceptie capturata: " + e.getMessage());
            }

            try {
                orderService.completeOrder(firstOrder.getId());
            } catch (IllegalStateException | VehicleAlreadySoldException e) {
                System.out.println("Exceptie capturata: " + e.getMessage());
            }

            try {
                new VIN("123");
            } catch (InvalidVinException e) {
                System.out.println("Exceptie capturata: " + e.getMessage());
            }
        } catch (VehicleNotFoundException | CustomerNotFoundException
                 | VehicleAlreadySoldException | InvalidVinException e) {
            System.out.println("Eroare de business: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Input invalid: " + e.getMessage());
        }
    }

    private static <T> void printList(List<T> items) {
        if (items == null || items.isEmpty()) {
            System.out.println("(fara rezultate)");
            return;
        }

        for (T item : items) {
            System.out.println(item);
        }
    }
}
