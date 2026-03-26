package etalons;

import functions.MathFunction;

public class LnEtalon implements MathFunction {
    @Override
    public double apply(double x) {
        if (Math.abs(x - 1) < 1e-12) return 0;
        if (Math.abs(x - 0.5) < 1e-12) return -0.693147180;
        if (Math.abs(x - Math.E) < 1e-12) return 1;
        if (Math.abs(x - 2) < 1e-12) return 0.69314718;
        if (Math.abs(x - 5) < 1e-12) return 1.60943791;
        if (Math.abs(x - 10) < 1e-12) return 2.30258509;
        throw new UnsupportedOperationException("Etalon ln не поддерживает x = " + x);
    }
}