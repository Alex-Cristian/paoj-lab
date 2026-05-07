package com.pao.laboratory09.exercise3;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        CoadaTranzactii coada = new CoadaTranzactii(5);
        AtomicInteger generatorId = new AtomicInteger(1);

        ProcessorThread processor = new ProcessorThread(coada);
        Thread processorThread = new Thread(processor, "Processor");

        ATMThread atm1 = new ATMThread(1, coada, generatorId);
        ATMThread atm2 = new ATMThread(2, coada, generatorId);
        ATMThread atm3 = new ATMThread(3, coada, generatorId);

        processorThread.start();
        atm1.start();
        atm2.start();
        atm3.start();

        atm1.join();
        atm2.join();
        atm3.join();

        processor.opreste();
        coada.trezesteToateFirele();
        processorThread.join();

        System.out.println("Toate tranzactiile procesate. Total: " + processor.getTotalProcesate());
    }

    private record Tranzactie(int id, double suma, String data) {
        private String descriere() {
            return String.format("Tranzactie #%d %.2f RON", id, suma);
        }
    }

    private static class CoadaTranzactii {
        private final Queue<Tranzactie> tranzactii = new LinkedList<>();
        private final int capacitate;

        private CoadaTranzactii(int capacitate) {
            this.capacitate = capacitate;
        }

        public synchronized void adauga(Tranzactie tranzactie, int atmId) throws InterruptedException {
            while (tranzactii.size() == capacitate) {
                System.out.println("[ATM-" + atmId + "] astept loc...");
                wait();
            }
            tranzactii.add(tranzactie);
            notifyAll();
        }

        public synchronized Tranzactie extrage(boolean activ) throws InterruptedException {
            while (tranzactii.isEmpty()) {
                if (!activ) {
                    return null;
                }
                wait();
            }
            Tranzactie tranzactie = tranzactii.poll();
            notifyAll();
            return tranzactie;
        }

        public synchronized void trezesteToateFirele() {
            notifyAll();
        }
    }

    private static class ATMThread extends Thread {
        private final int atmId;
        private final CoadaTranzactii coada;
        private final AtomicInteger generatorId;

        private ATMThread(int atmId, CoadaTranzactii coada, AtomicInteger generatorId) {
            this.atmId = atmId;
            this.coada = coada;
            this.generatorId = generatorId;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 4; i++) {
                    int id = generatorId.getAndIncrement();
                    Tranzactie tranzactie = new Tranzactie(id, 100.0 * atmId + 25.0 * i, "2024-01-" + String.format("%02d", id));
                    System.out.println("[ATM-" + atmId + "] trimite: " + tranzactie.descriere());
                    coada.adauga(tranzactie, atmId);
                    Thread.sleep(50);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static class ProcessorThread implements Runnable {
        private final CoadaTranzactii coada;
        private volatile boolean activ = true;
        private int totalProcesate;

        private ProcessorThread(CoadaTranzactii coada) {
            this.coada = coada;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Tranzactie tranzactie = coada.extrage(activ);
                    if (tranzactie == null) {
                        break;
                    }
                    Thread.sleep(80);
                    totalProcesate++;
                    System.out.printf("[Processor] Factura #%d - %.2f RON | %s%n",
                            tranzactie.id(), tranzactie.suma(), tranzactie.data());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private void opreste() {
            activ = false;
        }

        private int getTotalProcesate() {
            return totalProcesate;
        }
    }
}
