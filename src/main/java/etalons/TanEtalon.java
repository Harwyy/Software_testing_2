package etalons;

import functions.MathFunction;

public class TanEtalon implements MathFunction {
    @Override
    public double apply(double x) {
        if (Math.abs(x) < 1e-7) return 0;
        if (Math.abs(x - Math.PI) < 1e-7) return 0;
        if (Math.abs(x - Math.PI / 2) < 1e-7 || Math.abs(x + Math.PI / 2) < 1e-7) throw new ArithmeticException("tan undefined at x = +-pi/2");
        if (Math.abs(x - Math.PI / 3) < 1e-7) return 1.732050807;
        if (Math.abs(x + Math.PI / 3) < 1e-7) return -1.732050807;
        if (Math.abs(x - Math.PI / 4) < 1e-7) return 1;
        if (Math.abs(x + Math.PI / 4) < 1e-7) return -1;
        if (Math.abs(x - Math.PI / 6) < 1e-7) return 0.57735027;
        if (Math.abs(x + Math.PI / 6) < 1e-7) return -0.57735027;
        throw new UnsupportedOperationException("Etalon Tan не поддерживает x = " + x);
    }
}