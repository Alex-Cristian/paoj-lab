package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class PFAColaborator extends PersoanaFizica {
    private static final double SALARIU_MINIM_BRUT = 4050.0;

    private double cheltuieliLunare;

    public PFAColaborator() {
        super(TipColaborator.PFA);
    }

    @Override
    public void citeste(Scanner in) {
        setNume(in.next());
        setPrenume(in.next());
        setVenitBrutLunar(in.nextDouble());
        cheltuieliLunare = in.nextDouble();
    }

    @Override
    public String tipContract() {
        return "PFA";
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double venitNetInainteDeTaxe = (getVenitBrutLunar() - cheltuieliLunare) * 12;
        double impozit = 0.10 * venitNetInainteDeTaxe;
        double cass = 0.10 * (12 * SALARIU_MINIM_BRUT);
        double cas = 0.25 * (24 * SALARIU_MINIM_BRUT);
        return venitNetInainteDeTaxe - impozit - cass - cas;
    }
}
