package etalons;

import functions.MathFunction;

public class CosEtalon implements MathFunction {
    @Override
    public double apply(double x) {
        if (Math.abs(x) < 1e-7) return 1;
        if (Math.abs(x - Math.PI) < 1e-7) return -1;
        if (Math.abs(x - Math.PI / 2) < 1e-7) return 0;
        if (Math.abs(x + Math.PI / 2) < 1e-7) return 0;
        if (Math.abs(x - Math.PI / 3) < 1e-7) return 0.5;
        if (Math.abs(x + Math.PI / 3) < 1e-7) return 0.5;
        if (Math.abs(x - Math.PI / 4) < 1e-7) return 0.70710678;
        if (Math.abs(x + Math.PI / 4) < 1e-7) return 0.70710678;
        if (Math.abs(x - Math.PI / 6) < 1e-7) return 0.866025403;
        if (Math.abs(x + Math.PI / 6) < 1e-7) return 0.866025403;
        throw new UnsupportedOperationException("Etalon Cos не поддерживает x = " + x);
    }
}