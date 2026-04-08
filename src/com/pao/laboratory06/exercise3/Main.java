package com.pao.laboratory06.exercise3;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Inginer[] ingineri = {
                new Inginer("Popescu", "Ana", "0711111111", 12000, "ana.dev", "parola123", 3500),
                new Inginer("Ionescu", "Mihai", "0722222222", 9000, "mihai.dev", "secure456", 2800),
                new Inginer("Georgescu", "Radu", "0733333333", 15000, "radu.dev", "radu789", 5000)
        };

        Arrays.sort(ingineri);
        System.out.println("Sortare naturala dupa nume:");
        for (Inginer inginer : ingineri) {
            System.out.println(inginer);
        }

        Arrays.sort(ingineri, new ComparatorInginerSalariu());
        System.out.println();
        System.out.println("Sortare dupa salariu descrescator:");
        for (Inginer inginer : ingineri) {
            System.out.println(inginer);
        }

        PlataOnline plataOnline = ingineri[0];
        plataOnline.autentificare("radu.dev", "radu789");
        System.out.println();
        System.out.printf("Sold prin referinta PlataOnline: %.2f%n", plataOnline.consultareSold());
        System.out.println("Plata reusita: " + plataOnline.efectuarePlata(1200));
        System.out.printf("Sold dupa plata: %.2f%n", plataOnline.consultareSold());

        PersoanaJuridica companie = new PersoanaJuridica("Tech", "Solutions", "0744444444", "tech.srl", "firma123", 12000);
        PlataOnlineSMS plataSms = companie;
        plataSms.autentificare("tech.srl", "firma123");
        plataSms.efectuarePlata(2500);
        System.out.println();
        System.out.println("SMS valid trimis: " + plataSms.trimiteSMS("Plata de 2500 lei a fost procesata."));
        System.out.println("SMS invalid trimis: " + plataSms.trimiteSMS("   "));
        System.out.println("Mesaje stocate: " + companie.getSmsTrimise());

        PersoanaJuridica faraTelefon = new PersoanaJuridica("NoPhone", "SRL", "", "nophone", "test123", 3000);
        System.out.println("SMS catre companie fara telefon: " + faraTelefon.trimiteSMS("Test fara telefon"));

        System.out.println();
        System.out.println("TVA curent: " + ConstanteFinanciare.TVA.getValoare());

        try {
            plataOnline.trimiteSMS("Nu ar trebui sa mearga");
        } catch (UnsupportedOperationException e) {
            System.out.println("Eroare SMS pe entitate fara capabilitate SMS: " + e.getMessage());
        }

        try {
            plataOnline.autentificare(null, "ceva");
        } catch (IllegalArgumentException e) {
            System.out.println("Eroare autentificare invalida: " + e.getMessage());
        }
    }
}
