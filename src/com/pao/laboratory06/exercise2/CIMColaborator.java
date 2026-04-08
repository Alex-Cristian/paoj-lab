package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class CIMColaborator extends PersoanaFizica {
    private boolean bonus;

    public CIMColaborator() {
        super(TipColaborator.CIM);
    }

    @Override
    public void citeste(Scanner in) {
        setNume(in.next());
        setPrenume(in.next());
        setVenitBrutLunar(in.nextDouble());
        bonus = in.hasNext("DA|NU") && in.next().equals("DA");
    }

    @Override
    public String tipContract() {
        return "CIM";
    }

    @Override
    public boolean areBonus() {
        return bonus;
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double venitNet = getVenitBrutLunar() * 12 * 0.55;
        if (bonus) {
            venitNet *= 1.10;
        }
        return venitNet;
    }
}
