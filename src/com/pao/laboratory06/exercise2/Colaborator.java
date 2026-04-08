package com.pao.laboratory06.exercise2;

public abstract class Colaborator implements IOperatiiCitireScriere {
    private String nume;
    private String prenume;
    private double venitBrutLunar;
    private final TipColaborator tip;

    protected Colaborator(TipColaborator tip) {
        this.tip = tip;
    }

    public abstract double calculeazaVenitNetAnual();

    public TipColaborator getTip() {
        return tip;
    }

    protected void setNume(String nume) {
        this.nume = nume;
    }

    protected void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    protected void setVenitBrutLunar(double venitBrutLunar) {
        this.venitBrutLunar = venitBrutLunar;
    }

    public double getVenitBrutLunar() {
        return venitBrutLunar;
    }

    public String getNumeComplet() {
        return nume + " " + prenume;
    }

    @Override
    public void afiseaza() {
        System.out.printf("%s: %s, venit net anual: %.2f lei%n", tipContract(), getNumeComplet(), calculeazaVenitNetAnual());
    }
}
