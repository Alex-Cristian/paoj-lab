package com.pao.laboratory07.exercise2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Alege modul de rulare: 0 = demo, 1 = input manual");
        int optiune = Integer.parseInt(scanner.nextLine().trim());

        List<String> liniiInput;
        if (optiune == 0) {
            liniiInput = demoInput();
        } else {
            System.out.println("Introdu numarul de comenzi, apoi comenzile:");
            int n = Integer.parseInt(scanner.nextLine().trim());
            liniiInput = new ArrayList<>();
            liniiInput.add(String.valueOf(n));
            for (int i = 0; i < n; i++) {
                liniiInput.add(scanner.nextLine());
            }
        }

        int n = Integer.parseInt(liniiInput.get(0).trim());

        List<Comanda> comenzi = new ArrayList<>();
        double sumaStandard = 0;
        double sumaDiscounted = 0;
        double sumaGift = 0;
        int nrStandard = 0;
        int nrDiscounted = 0;
        int nrGift = 0;

        for (int i = 0; i < n; i++) {
            String[] tokens = liniiInput.get(i + 1).trim().split("\\s+");
            Comanda comanda = switch (tokens[0]) {
                case "STANDARD" -> new ComandaStandard(tokens[1], Double.parseDouble(tokens[2]));
                case "DISCOUNTED" -> new ComandaRedusa(tokens[1], Double.parseDouble(tokens[2]), Integer.parseInt(tokens[3]));
                case "GIFT" -> new ComandaGratuita(tokens[1]);
                default -> throw new IllegalArgumentException("Tip necunoscut");
            };
            comenzi.add(comanda);

            if (comanda instanceof ComandaStandard) {
                nrStandard++;
                sumaStandard += comanda.pretFinal();
            } else if (comanda instanceof ComandaRedusa) {
                nrDiscounted++;
                sumaDiscounted += comanda.pretFinal();
            } else if (comanda instanceof ComandaGratuita) {
                nrGift++;
                sumaGift += comanda.pretFinal();
            }
        }

        for (Comanda comanda : comenzi) {
            System.out.println(comanda.descriere());
        }

        System.out.println();
        System.out.println("Statistici:");
        if (nrStandard > 0) {
            System.out.printf("STANDARD: suma = %.2f lei, numar = %d%n", sumaStandard, nrStandard);
        }
        if (nrDiscounted > 0) {
            System.out.printf("DISCOUNTED: suma = %.2f lei, numar = %d%n", sumaDiscounted, nrDiscounted);
        }
        if (nrGift > 0) {
            System.out.printf("GIFT: suma = %.2f lei, numar = %d%n", sumaGift, nrGift);
        }
        System.out.printf("Total platit: %.2f lei%n", sumaStandard + sumaDiscounted + sumaGift);
    }

    private static List<String> demoInput() {
        return Arrays.asList(
                "4",
                "STANDARD Laptop 2500.0",
                "DISCOUNTED Headphones 200.0 20",
                "GIFT Sticker",
                "STANDARD Mouse 80.0"
        );
    }
}
