package com.pao.laboratory09.exercise1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex1.ser";

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) {
            return;
        }

        int n = scanner.nextInt();
        List<Tranzactie> tranzactii = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Tranzactie tranzactie = new Tranzactie(
                    scanner.nextInt(),
                    scanner.nextDouble(),
                    scanner.next(),
                    scanner.next(),
                    scanner.next(),
                    TipTranzactie.valueOf(scanner.next()));
            tranzactie.setNote("procesat");
            tranzactii.add(tranzactie);
        }

        new File("output").mkdirs();
        serializeaza(tranzactii);
        List<Tranzactie> restaurate = deserializeaza();
        proceseazaComenzi(scanner, restaurate);
    }

    private static void serializeaza(List<Tranzactie> tranzactii) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            out.writeObject(tranzactii);
        }
    }

    @SuppressWarnings("unchecked")
    private static List<Tranzactie> deserializeaza() throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(OUTPUT_FILE))) {
            return (List<Tranzactie>) in.readObject();
        }
    }

    private static void proceseazaComenzi(Scanner scanner, List<Tranzactie> tranzactii) {
        while (scanner.hasNext()) {
            String comanda = scanner.next();
            switch (comanda) {
                case "LIST" -> afiseazaLista(tranzactii);
                case "FILTER" -> filtreazaDupaLuna(scanner.next(), tranzactii);
                case "NOTE" -> afiseazaNota(scanner.nextInt(), tranzactii);
                default -> {
                }
            }
        }
    }

    private static void afiseazaLista(List<Tranzactie> tranzactii) {
        for (Tranzactie tranzactie : tranzactii) {
            System.out.println(tranzactie.format());
        }
    }

    private static void filtreazaDupaLuna(String luna, List<Tranzactie> tranzactii) {
        boolean gasit = false;
        for (Tranzactie tranzactie : tranzactii) {
            if (tranzactie.getData().startsWith(luna)) {
                System.out.println(tranzactie.format());
                gasit = true;
            }
        }
        if (!gasit) {
            System.out.println("Niciun rezultat.");
        }
    }

    private static void afiseazaNota(int id, List<Tranzactie> tranzactii) {
        for (Tranzactie tranzactie : tranzactii) {
            if (tranzactie.getId() == id) {
                System.out.println("NOTE[" + id + "]: " + tranzactie.getNote());
                return;
            }
        }
        System.out.println("NOTE[" + id + "]: not found");
    }
}
