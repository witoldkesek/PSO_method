package com.company;

import java.util.Arrays;

public class Punkt {
    double []a;

    @Override
    public String toString() {
        return "Punkt{" +
                "a=" + Arrays.toString(a) +
                '}';
    }

    public Punkt(double[] a) {
        this.a = a;
    }
    public Punkt(Punkt p){
        a= new double[]{0, 0, 0, 0, 0, 0, 0};
        System.arraycopy(p.a, 0, this.a, 0, p.a.length);

    }
}
