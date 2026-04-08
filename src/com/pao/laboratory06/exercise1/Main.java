package com.pao.laboratory06.exercise1;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Vezi Readme.md pentru cerințe
        Scanner scanner = new Scanner(System.in);
        String optiune = scanner.next();
        int numarAngajati = scanner.nextInt();
        Angajat[] angajati = new Angajat[numarAngajati];
        for (int i = 0; i < numarAngajati; i++) {
            angajati[i] = Angajat.citeste(scanner);
        }
        switch (optiune) {
            case "by_salary" -> Arrays.sort(angajati);
            case "by_name" -> Arrays.sort(angajati, Comparator.comparing(Angajat::getNume));
            case "by_salary_desc" -> Arrays.sort(angajati, Comparator.comparingDouble(Angajat::getSalariu).reversed());
            default -> {
            }
        }
        for (Angajat angajat : angajati) {
            System.out.println(angajat);
        }
    }
}
