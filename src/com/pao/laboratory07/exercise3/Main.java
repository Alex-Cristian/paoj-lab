package com.pao.laboratory07.exercise3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Alege modul de rulare: 0 = demo, 1 = input manual");
        int optiune = Integer.parseInt(scanner.nextLine().trim());

        List<String> liniiInput;
        if (optiune == 0) {
            liniiInput = demoInput();
        } else {
            liniiInput = new ArrayList<>();
            System.out.println("Introdu numarul de comenzi:");
            int n = Integer.parseInt(scanner.nextLine().trim());
            liniiInput.add(String.valueOf(n));
            System.out.println("Introdu comenzile initiale:");
            for (int i = 0; i < n; i++) {
                liniiInput.add(scanner.nextLine());
            }
            System.out.println("Introdu actiuni precum STATS, FILTER <prag>, SORT, SPECIAL, QUIT:");
            while (scanner.hasNextLine()) {
                String linie = scanner.nextLine();
                liniiInput.add(linie);
                if ("QUIT".equals(linie.trim())) {
                    break;
                }
            }
        }

        int n = Integer.parseInt(liniiInput.get(0).trim());
        List<Comanda> comenzi = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String[] tokens = liniiInput.get(i + 1).trim().split("\\s+");
            Comanda comanda = switch (tokens[0]) {
                case "STANDARD" -> new ComandaStandard(tokens[1], Double.parseDouble(tokens[2]), tokens[3]);
                case "DISCOUNTED" -> new ComandaRedusa(tokens[1], Double.parseDouble(tokens[2]), Integer.parseInt(tokens[3]), tokens[4]);
                case "GIFT" -> new ComandaGratuita(tokens[1], tokens[2]);
                default -> throw new IllegalArgumentException("Tip necunoscut");
            };
            comenzi.add(comanda);
        }

        for (Comanda comanda : comenzi) {
            System.out.println(comanda.descriereCompleta());
        }

        for (int index = n + 1; index < liniiInput.size(); index++) {
            String line = liniiInput.get(index).trim();
            if (line.isEmpty()) {
                continue;
            }
            if (line.equals("QUIT")) {
                return;
            }
            if (line.equals("STATS")) {
                System.out.println();
                System.out.println("--- STATS ---");
                Map<String, Double> medii = comenzi.stream()
                        .collect(Collectors.groupingBy(Comanda::tip, Collectors.averagingDouble(Comanda::pretFinal)));
                printAverageIfPresent(medii, "STANDARD");
                printAverageIfPresent(medii, "DISCOUNTED");
                printAverageIfPresent(medii, "GIFT");
            } else if (line.startsWith("FILTER ")) {
                double prag = Double.parseDouble(line.substring(7).trim());
                System.out.println();
                System.out.printf("--- FILTER (>= %.2f) ---%n", prag);
                comenzi.stream()
                        .filter(comanda -> comanda.pretFinal() >= prag)
                        .forEach(comanda -> System.out.println(comanda.descriereScurta()));
            } else if (line.equals("SORT")) {
                System.out.println();
                System.out.println("--- SORT (by client, then by pret) ---");
                comenzi.stream()
                        .sorted(Comparator.comparing(Comanda::getClient).thenComparing(Comanda::pretFinal))
                        .forEach(comanda -> System.out.println(comanda.descriereScurta()));
            } else if (line.equals("SPECIAL")) {
                System.out.println();
                System.out.println("--- SPECIAL (discount > 15%) ---");
                comenzi.stream()
                        .filter(comanda -> comanda instanceof ComandaRedusa redusa && redusa.getDiscountProcent() > 15)
                        .forEach(comanda -> System.out.println(comanda.descriereScurta()));
            }
        }
    }

    private static List<String> demoInput() {
        return Arrays.asList(
                "5",
                "STANDARD Laptop 2500.0 Alice",
                "DISCOUNTED Headphones 200.0 20 Bob",
                "GIFT Sticker Charlie",
                "STANDARD Mouse 80.0 Alice",
                "DISCOUNTED Keyboard 300.0 10 Dave",
                "STATS",
                "FILTER 100",
                "SORT",
                "SPECIAL",
                "QUIT"
        );
    }

    private static void printAverageIfPresent(Map<String, Double> medii, String tip) {
        if (medii.containsKey(tip)) {
            System.out.printf("%s: medie = %.2f lei%n", tip, medii.get(tip));
        }
    }
}
