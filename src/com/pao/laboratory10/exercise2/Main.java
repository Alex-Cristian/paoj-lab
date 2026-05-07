package com.pao.laboratory10.exercise2;

import com.pao.laboratory10.exercise1.TipTranzactie;
import com.pao.laboratory10.exercise1.Tranzactie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) {
            return;
        }

        int n = scanner.nextInt();
        List<Tranzactie> tranzactii = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            tranzactii.add(new Tranzactie(
                    scanner.nextInt(),
                    scanner.nextDouble(),
                    scanner.next(),
                    TipTranzactie.valueOf(scanner.next())));
        }

        while (scanner.hasNext()) {
            String comanda = scanner.next();
            switch (comanda) {
                case "UNIQUE_IDS" -> afiseazaIdUnice(tranzactii);
                case "MONTHLY_REPORT" -> afiseazaRaportLunar(tranzactii);
                case "TOP" -> afiseazaTop(tranzactii, scanner.nextInt());
                case "SORT_ASC" -> {
                    Collections.sort(tranzactii, Comparator.comparingDouble(Tranzactie::getSuma));
                    afiseazaLista(tranzactii);
                }
                case "SORT_DESC" -> {
                    Collections.sort(tranzactii, Comparator.comparingDouble(Tranzactie::getSuma).reversed());
                    afiseazaLista(tranzactii);
                }
                case "REVERSE" -> {
                    Collections.reverse(tranzactii);
                    afiseazaLista(tranzactii);
                }
                case "MIN_MAX" -> afiseazaMinMax(tranzactii);
                case "CME_DEMO" -> demoConcurrentModification(tranzactii);
                default -> {
                }
            }
        }
    }

    private static void afiseazaIdUnice(List<Tranzactie> tranzactii) {
        Set<Integer> ids = new LinkedHashSet<>();
        for (Tranzactie tranzactie : tranzactii) {
            ids.add(tranzactie.getId());
        }
        System.out.println("IDs unice (" + ids.size() + "): " + ids);
    }

    private static void afiseazaRaportLunar(List<Tranzactie> tranzactii) {
        TreeMap<String, double[]> raport = new TreeMap<>();
        for (Tranzactie tranzactie : tranzactii) {
            String luna = tranzactie.getData().substring(0, 7);
            double[] totaluri = raport.computeIfAbsent(luna, key -> new double[2]);
            if (tranzactie.getTip() == TipTranzactie.CREDIT) {
                totaluri[0] += tranzactie.getSuma();
            } else {
                totaluri[1] += tranzactie.getSuma();
            }
        }

        for (var entry : raport.entrySet()) {
            double[] totaluri = entry.getValue();
            System.out.printf("%s: CREDIT %.2f RON, DEBIT %.2f RON%n",
                    entry.getKey(), totaluri[0], totaluri[1]);
        }
    }

    private static void afiseazaTop(List<Tranzactie> tranzactii, int n) {
        List<Tranzactie> copie = new ArrayList<>(tranzactii);
        copie.sort(Comparator.comparingDouble(Tranzactie::getSuma).reversed());

        System.out.println("Top " + n + ":");
        for (int i = 0; i < Math.min(n, copie.size()); i++) {
            System.out.println(copie.get(i));
        }
    }

    private static void afiseazaMinMax(List<Tranzactie> tranzactii) {
        if (tranzactii.isEmpty()) {
            return;
        }
        Comparator<Tranzactie> dupaSuma = Comparator.comparingDouble(Tranzactie::getSuma);
        System.out.println("MIN: " + Collections.min(tranzactii, dupaSuma));
        System.out.println("MAX: " + Collections.max(tranzactii, dupaSuma));
    }

    private static void demoConcurrentModification(List<Tranzactie> tranzactii) {
        try {
            List<Tranzactie> copie = new ArrayList<>(tranzactii);
            for (Tranzactie tranzactie : copie) {
                copie.remove(tranzactie);
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("ConcurrentModificationException prins: modificare in iteratie detectata.");
        }
    }

    private static void afiseazaLista(List<Tranzactie> tranzactii) {
        for (Tranzactie tranzactie : tranzactii) {
            System.out.println(tranzactie);
        }
    }
}
