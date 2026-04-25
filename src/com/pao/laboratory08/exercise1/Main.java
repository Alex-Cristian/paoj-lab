package com.pao.laboratory08.exercise1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Alege modul de rulare: 0 = demo, 1 = input manual");
        int optiune = Integer.parseInt(scanner.nextLine().trim());

        String commandLine;
        if (optiune == 0) {
            commandLine = demoCommand();
            System.out.println("Rulez demo-ul predefinit.");
        } else {
            System.out.println("Introdu comanda: PRINT, SHALLOW <nume> sau DEEP <nume>");
            commandLine = scanner.nextLine().trim();
        }

        List<Student> studenti = readStudents();

        if (commandLine.equals("PRINT")) {
            for (Student student : studenti) {
                System.out.println(student);
            }
            return;
        }

        String[] parts = commandLine.split(" ", 2);
        String command = parts[0];
        String name = parts.length > 1 ? parts[1].trim() : "";
        Student original = findByName(studenti, name);
        if (original == null) {
            return;
        }

        try {
            Student clone;
            if (command.equals("SHALLOW")) {
                clone = original.shallowClone();
            } else if (command.equals("DEEP")) {
                clone = original.deepClone();
            } else {
                return;
            }

            clone.getAdresa().setOras("MODIFICAT");
            System.out.println("Original: " + original);
            System.out.println("Clona: " + clone);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Student> readStudents() {
        List<Student> studenti = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split(",");
                String nume = parts[0].trim();
                int varsta = Integer.parseInt(parts[1].trim());
                String oras = parts[2].trim();
                String strada = parts[3].trim();
                studenti.add(new Student(nume, varsta, new Adresa(oras, strada)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return studenti;
    }

    private static Student findByName(List<Student> studenti, String name) {
        for (Student student : studenti) {
            if (student.getNume().equals(name)) {
                return student;
            }
        }
        return null;
    }

    private static String demoCommand() {
        List<String> demoCommands = Arrays.asList(
                "PRINT",
                "SHALLOW Ana",
                "DEEP Ana"
        );
        return demoCommands.get(0);
    }
}
