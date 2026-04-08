package com.pao.laboratory06.exercise3;

public class Inginer extends Angajat implements PlataOnline, Comparable<Inginer> {
    private final String username;
    private final String parola;
    private double sold;
    private boolean autentificat;

    public Inginer(String nume, String prenume, String telefon, double salariu, String username, String parola, double sold) {
        super(nume, prenume, telefon, salariu);
        if (Persoana.esteNullSauGol(username) || Persoana.esteNullSauGol(parola)) {
            throw new IllegalArgumentException("Credentialele sunt obligatorii.");
        }
        if (sold < 0) {
            throw new IllegalArgumentException("Soldul initial nu poate fi negativ.");
        }
        this.username = username;
        this.parola = parola;
        this.sold = sold;
    }

    @Override
    public void autentificare(String user, String parola) {
        if (Persoana.esteNullSauGol(user) || Persoana.esteNullSauGol(parola)) {
            throw new IllegalArgumentException("Userul si parola sunt obligatorii.");
        }
        autentificat = username.equals(user) && this.parola.equals(parola);
    }

    @Override
    public double consultareSold() {
        return sold;
    }

    @Override
    public boolean efectuarePlata(double suma) {
        if (suma <= 0) {
            throw new IllegalArgumentException("Suma trebuie sa fie pozitiva.");
        }
        if (!autentificat || suma > sold) {
            return false;
        }
        sold -= suma;
        return true;
    }

    @Override
    public int compareTo(Inginer other) {
        int rezultat = getNume().compareTo(other.getNume());
        if (rezultat != 0) {
            return rezultat;
        }
        return getPrenume().compareTo(other.getPrenume());
    }

    @Override
    public String toString() {
        return String.format("Inginer %s, salariu %.2f, sold %.2f", getNumeComplet(), getSalariu(), sold);
    }
}
