package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) {
            return;
        }

        int n = scanner.nextInt();
        List<Inregistrare> inregistrari = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            inregistrari.add(new Inregistrare(
                    scanner.nextInt(),
                    scanner.nextDouble(),
                    scanner.next(),
                    TipTranzactie.valueOf(scanner.next()),
                    Status.PENDING));
        }

        new File("output").mkdirs();
        scrieInitial(inregistrari);

        try (RandomAccessFile raf = new RandomAccessFile(OUTPUT_FILE, "rw")) {
            while (scanner.hasNext()) {
                String comanda = scanner.next();
                switch (comanda) {
                    case "READ" -> {
                        int idx = scanner.nextInt();
                        System.out.println(citeste(raf, idx).format(idx));
                    }
                    case "UPDATE" -> {
                        int idx = scanner.nextInt();
                        Status status = Status.valueOf(scanner.next());
                        actualizeazaStatus(raf, idx, status);
                        System.out.println("Updated [" + idx + "]: " + status);
                    }
                    case "PRINT_ALL" -> {
                        for (int idx = 0; idx < n; idx++) {
                            System.out.println(citeste(raf, idx).format(idx));
                        }
                    }
                    default -> {
                    }
                }
            }
        }
    }

    private static void scrieInitial(List<Inregistrare> inregistrari) throws IOException {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            for (Inregistrare inregistrare : inregistrari) {
                out.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(inregistrare.id()).array());
                out.write(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putDouble(inregistrare.suma()).array());

                byte[] dataBytes = Arrays.copyOf(inregistrare.data().getBytes(), 10);
                for (int i = inregistrare.data().length(); i < dataBytes.length; i++) {
                    dataBytes[i] = ' ';
                }
                out.write(dataBytes);

                out.writeByte(inregistrare.tip() == TipTranzactie.CREDIT ? 0 : 1);
                out.writeByte(inregistrare.status().cod);
                out.write(new byte[8]);
            }
        }
    }

    private static Inregistrare citeste(RandomAccessFile raf, int idx) throws IOException {
        byte[] bytes = new byte[RECORD_SIZE];
        raf.seek((long) idx * RECORD_SIZE);
        raf.readFully(bytes);

        ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        int id = buffer.getInt();
        double suma = buffer.getDouble();
        byte[] dataBytes = new byte[10];
        buffer.get(dataBytes);
        String data = new String(dataBytes).trim();
        TipTranzactie tip = buffer.get() == 0 ? TipTranzactie.CREDIT : TipTranzactie.DEBIT;
        Status status = Status.fromCod(buffer.get());

        return new Inregistrare(id, suma, data, tip, status);
    }

    private static void actualizeazaStatus(RandomAccessFile raf, int idx, Status status) throws IOException {
        raf.seek((long) idx * RECORD_SIZE + 23);
        raf.write(status.cod);
    }

    private enum Status {
        PENDING(0),
        PROCESSED(1),
        REJECTED(2);

        private final int cod;

        Status(int cod) {
            this.cod = cod;
        }

        private static Status fromCod(byte cod) {
            for (Status status : values()) {
                if (status.cod == cod) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Status invalid: " + cod);
        }
    }

    private record Inregistrare(int id, double suma, String data, TipTranzactie tip, Status status) {
        private String format(int idx) {
            return String.format("[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s",
                    idx, id, data, tip, suma, status);
        }
    }
}
