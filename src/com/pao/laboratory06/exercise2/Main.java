package com.pao.laboratory06.exercise2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        List<Colaborator> colaboratori = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String tip = in.next();
            Colaborator colaborator = switch (tip) {
                case "CIM" -> new CIMColaborator();
                case "PFA" -> new PFAColaborator();
                case "SRL" -> new SRLColaborator();
                default -> throw new IllegalArgumentException("Tip necunoscut: " + tip);
            };
            colaborator.citeste(in);
            colaboratori.add(colaborator);
        }

        Comparator<Colaborator> dupaVenitDesc =
                Comparator.comparingDouble(Colaborator::calculeazaVenitNetAnual).reversed();

        for (TipColaborator tip : TipColaborator.values()) {
            colaboratori.stream()
                    .filter(colaborator -> colaborator.getTip() == tip)
                    .sorted(dupaVenitDesc)
                    .forEach(Colaborator::afiseaza);
        }

        Colaborator maxim = colaboratori.stream()
                .max(Comparator.comparingDouble(Colaborator::calculeazaVenitNetAnual))
                .orElse(null);
        System.out.println();
        System.out.print("Colaborator cu venit net maxim: ");
        if (maxim != null) {
            maxim.afiseaza();
        }

        System.out.println();
        System.out.println("Colaboratori persoane juridice:");
        colaboratori.stream()
                .filter(colaborator -> colaborator instanceof PersoanaJuridica)
                .sorted(dupaVenitDesc)
                .forEach(Colaborator::afiseaza);

        Map<TipColaborator, Double> sume = new EnumMap<>(TipColaborator.class);
        Map<TipColaborator, Integer> numere = new EnumMap<>(TipColaborator.class);
        for (Colaborator colaborator : colaboratori) {
            TipColaborator tip = colaborator.getTip();
            sume.put(tip, sume.getOrDefault(tip, 0.0) + colaborator.calculeazaVenitNetAnual());
            numere.put(tip, numere.getOrDefault(tip, 0) + 1);
        }

        System.out.println();
        System.out.println("Sume \u0219i num\u0103r colaboratori pe tip:");
        for (TipColaborator tip : TipColaborator.values()) {
            Double suma = sume.get(tip);
            Integer numar = numere.get(tip);
            String sumaText = suma == null ? "nu" : String.format("%.2f", suma);
            String numarText = String.valueOf(numar);
            System.out.printf("%s: suma = %s lei, num\u0103r = %s%n", tip, sumaText, numarText);
        }
        return;
//        Scanner in = new Scanner(System.in);
//        int n = in.nextInt();
//        List<Colaborator> colaboratori = new ArrayList<>();
//        for (int i = 0; i < n; i++) {
//            String tip = in.next();
//            Colaborator c = switch (tip) {
//                case "CIM" -> {
//                    CIMColaborator obj = new CIMColaborator();
//                    obj.citeste(in);
//                    yield obj;
//                }
//                case "PFA" -> {
//                    PFAColaborator obj = new PFAColaborator();
//                    obj.citeste(in);
//                    yield obj;
//                }
//                case "SRL" -> {
//                    SRLColaborator obj = new SRLColaborator();
//                    obj.citeste(in);
//                    yield obj;
//                }
//                default -> throw new IllegalArgumentException("Tip necunoscut: " + tip);
//            };
//            colaboratori.add(c);
//        }
//        // Sortează și afișează pe tip, fiecare descrescător după venit net anual
//        for (TipColaborator tipColab : TipColaborator.values()) {
//            colaboratori.stream()
//                    .filter(c -> c.getTip() == tipColab)
//                    .sorted((a, b) -> Double.compare(b.calculeazaVenitNetAnual(), a.calculeazaVenitNetAnual()))
//                    .forEach(Colaborator::afiseaza);
//        }
//        // Colaborator cu venit net maxim
//        Colaborator max = colaboratori.stream().max(Comparator.comparingDouble(Colaborator::calculeazaVenitNetAnual)).orElse(null);
//        System.out.printf("\nColaborator cu venit net maxim: ");
//        if (max != null) max.afiseaza();
//        // Colaboratori persoane juridice (SRL)
//        System.out.println("\nColaboratori persoane juridice:");
//        colaboratori.stream()
//                .filter(c -> c instanceof PersoanaJuridica)
//                .sorted((a, b) -> Double.compare(b.calculeazaVenitNetAnual(), a.calculeazaVenitNetAnual()))
//                .forEach(Colaborator::afiseaza);
//        // Sume și număr colaboratori pe tip
//        System.out.println("\nSume și număr colaboratori pe tip:");
//        Map<TipColaborator, Double> suma = new EnumMap<>(TipColaborator.class);
//        Map<TipColaborator, Integer> numar = new EnumMap<>(TipColaborator.class);
//        var typesOfCollaborators = new HashSet<TipColaborator>();
//        for (Colaborator c : colaboratori) {
//            typesOfCollaborators.add(c.getTip());
//        }
//        for (TipColaborator t : typesOfCollaborators) {
//            suma.put(t, 0.0);
//            numar.put(t, 0);
//        }
//        for (Colaborator c : colaboratori) {
//            TipColaborator t = c.getTip();
//            suma.put(t, suma.get(t) + c.calculeazaVenitNetAnual());
//            numar.put(t, numar.get(t) + 1);
//        }
//        for (TipColaborator t : TipColaborator.values()) {
//            System.out.printf("%s: suma = %.2f lei, număr = %d\n", t, suma.get(t), numar.get(t));
//        }
    }
}
