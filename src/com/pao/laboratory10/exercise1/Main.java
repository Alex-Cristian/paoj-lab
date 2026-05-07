package com.pao.laboratory10.exercise1;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LinkedList<Tranzactie> coada = new LinkedList<>();

        while (scanner.hasNext()) {
            String comanda = scanner.next();
            switch (comanda) {
                case "ENQUEUE" -> coada.addLast(citesteTranzactie(scanner));
                case "DEQUEUE" -> proceseazaPrimul(coada, "Procesat: ");
                case "PUSH" -> coada.addFirst(citesteTranzactie(scanner));
                case "POP" -> proceseazaPrimul(coada, "Extras: ");
                case "REMOVE_DEBIT" -> eliminaDebit(coada);
                case "REMOVE_BELOW" -> eliminaSubPrag(coada, scanner.nextDouble());
                case "PRINT" -> afiseaza(coada);
                case "SIZE" -> System.out.println("Dimensiune coada: " + coada.size());
                default -> {
                }
            }
        }
    }

    private static Tranzactie citesteTranzactie(Scanner scanner) {
        return new Tranzactie(
                scanner.nextInt(),
                scanner.nextDouble(),
                scanner.next(),
                TipTranzactie.valueOf(scanner.next()));
    }

    private static void proceseazaPrimul(LinkedList<Tranzactie> coada, String prefix) {
        if (coada.isEmpty()) {
            System.out.println("Coada goala.");
            return;
        }
        System.out.println(prefix + coada.removeFirst());
    }

    private static void eliminaDebit(LinkedList<Tranzactie> coada) {
        int eliminate = 0;
        Iterator<Tranzactie> iterator = coada.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getTip() == TipTranzactie.DEBIT) {
                iterator.remove();
                eliminate++;
            }
        }
        System.out.println("Eliminat " + eliminate + " tranzactii DEBIT.");
    }

    private static void eliminaSubPrag(LinkedList<Tranzactie> coada, double prag) {
        int eliminate = 0;
        Iterator<Tranzactie> iterator = coada.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getSuma() < prag) {
                iterator.remove();
                eliminate++;
            }
        }
        System.out.printf("Eliminat %d tranzactii sub %.2f RON.%n", eliminate, prag);
    }

    private static void afiseaza(LinkedList<Tranzactie> coada) {
        for (Tranzactie tranzactie : coada) {
            System.out.println(tranzactie);
        }
    }
}
