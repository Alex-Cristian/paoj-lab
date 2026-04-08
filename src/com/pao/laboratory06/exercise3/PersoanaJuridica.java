package com.pao.laboratory06.exercise3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PersoanaJuridica extends Persoana implements PlataOnlineSMS {
    private final List<String> smsTrimise = new ArrayList<>();
    private final String username;
    private final String parola;
    private double sold;
    private boolean autentificat;

    public PersoanaJuridica(String nume, String prenume, String telefon, String username, String parola, double sold) {
        super(nume, prenume, telefon);
        if (esteNullSauGol(username) || esteNullSauGol(parola)) {
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
        if (esteNullSauGol(user) || esteNullSauGol(parola)) {
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
    public boolean trimiteSMS(String mesaj) {
        if (mesaj == null || mesaj.isBlank()) {
            return false;
        }
        if (getTelefon() == null || getTelefon().isBlank()) {
            return false;
        }
        smsTrimise.add(mesaj);
        return true;
    }

    public List<String> getSmsTrimise() {
        return Collections.unmodifiableList(smsTrimise);
    }

    @Override
    public String toString() {
        return String.format("Persoana juridica %s, sold %.2f, sms=%s", getNumeComplet(), sold, smsTrimise);
    }
}
