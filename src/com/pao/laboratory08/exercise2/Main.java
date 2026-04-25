package com.pao.laboratory08.exercise2;

import com.pao.laboratory08.exercise1.Student;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String OUTPUT_FILE = "rezultate.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Alege modul de rulare: 0 = demo, 1 = input manual");
        int optiune = Integer.parseInt(scanner.nextLine().trim());

        int prag;
        if (optiune == 0) {
            prag = 20;
            System.out.println("Rulez demo-ul predefinit.");
        } else {
            System.out.println("Introdu pragul minim de varsta:");
            prag = Integer.parseInt(scanner.nextLine().trim());
        }

        List<Student> studenti = com.pao.laboratory08.exercise1.Main.readStudents();

        List<Student> filtrati = new ArrayList<>();
        for (Student student : studenti) {
            if (student.getVarsta() >= prag) {
                filtrati.add(student);
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE))) {
            for (Student student : filtrati) {
                writer.write(student.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Filtru: varsta >= " + prag);
        System.out.println("Rezultate: " + filtrati.size() + " studenti");
        System.out.println();
        for (Student student : filtrati) {
            System.out.println(student);
        }
        System.out.println();
        System.out.println("Scris in: " + OUTPUT_FILE);
    }
}
