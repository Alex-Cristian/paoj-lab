package com.pao.laboratory10.exercise3;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Tranzactie> tranzactii = List.of(
                new Tranzactie(1, 1500.00, "2024-01-15", TipTranzactie.CREDIT, "RO01SRC1"),
                new Tranzactie(2, 750.50, "2024-01-22", TipTranzactie.DEBIT, "RO02SRC2"),
                new Tranzactie(3, 200.00, "2024-02-05", TipTranzactie.CREDIT, "RO01SRC1"),
                new Tranzactie(4, 1200.00, "2024-02-18", TipTranzactie.DEBIT, "RO03SRC3"),
                new Tranzactie(5, 500.00, "2024-03-10", TipTranzactie.CREDIT, "RO04SRC4"),
                new Tranzactie(6, 300.00, "2024-03-22", TipTranzactie.DEBIT, "RO02SRC2"),
                new Tranzactie(7, 2200.00, "2024-01-29", TipTranzactie.CREDIT, "RO05SRC5"),
                new Tranzactie(8, 125.75, "2024-02-20", TipTranzactie.DEBIT, "RO01SRC1"),
                new Tranzactie(9, 950.00, "2024-03-25", TipTranzactie.CREDIT, "RO03SRC3"),
                new Tranzactie(10, 80.00, "2024-04-02", TipTranzactie.DEBIT, "RO06SRC6"));

        System.out.println("1. Tranzactii CREDIT");
        tranzactii.stream()
                .filter(t -> t.tip() == TipTranzactie.CREDIT)
                .forEach(System.out::println);

        System.out.println();
        System.out.println("2. Total procesat");
        double total = tranzactii.stream().mapToDouble(Tranzactie::suma).sum();
        System.out.printf("Total procesat: %.2f RON%n", total);

        System.out.println();
        System.out.println("3. Total pe luna");
        Map<String, Double> totalPeLuna = tranzactii.stream()
                .collect(Collectors.groupingBy(
                        Tranzactie::luna,
                        TreeMap::new,
                        Collectors.summingDouble(Tranzactie::suma)));
        totalPeLuna.forEach((luna, suma) -> System.out.printf("%s: %.2f RON%n", luna, suma));

        System.out.println();
        System.out.println("4. Top 3 tranzactii");
        System.out.println("Top 3 tranzactii:");
        tranzactii.stream()
                .sorted(Comparator.comparingDouble(Tranzactie::suma).reversed())
                .limit(3)
                .forEach(System.out::println);

        System.out.println();
        System.out.println("5. Conturi sursa unice");
        List<String> conturi = tranzactii.stream()
                .map(Tranzactie::contSursa)
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Conturi sursa unice: " + conturi);

        System.out.println();
        System.out.println("6. Suma medie");
        double medie = tranzactii.stream().mapToDouble(Tranzactie::suma).average().orElse(0.0);
        System.out.printf("Suma medie: %.2f RON%n", medie);

        System.out.println();
        System.out.println("7. Extrase de cont lunare");
        Map<String, List<Tranzactie>> peLuni = tranzactii.stream()
                .collect(Collectors.groupingBy(Tranzactie::luna, TreeMap::new, Collectors.toList()));
        peLuni.forEach((luna, lista) -> {
            double suma = lista.stream().mapToDouble(Tranzactie::suma).sum();
            System.out.printf("EXTRAS DE CONT - %s: %d tranzactii, total: %.2f RON%n",
                    luna, lista.size(), suma);
        });
    }

    private enum TipTranzactie {
        CREDIT,
        DEBIT
    }

    private record Tranzactie(int id, double suma, String data, TipTranzactie tip, String contSursa) {
        private String luna() {
            return data.substring(0, 7);
        }

        @Override
        public String toString() {
            return String.format("[%d] %s %s: %.2f RON | contSursa=%s",
                    id, data, tip, suma, contSursa);
        }
    }
}
