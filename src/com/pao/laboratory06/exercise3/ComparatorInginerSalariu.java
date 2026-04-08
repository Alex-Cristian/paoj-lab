package com.pao.laboratory06.exercise3;

import java.util.Comparator;

public class ComparatorInginerSalariu implements Comparator<Inginer> {
    @Override
    public int compare(Inginer left, Inginer right) {
        return Double.compare(right.getSalariu(), left.getSalariu());
    }
}
