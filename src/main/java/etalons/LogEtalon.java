package etalons;

import functions.MathFunction;

public class LogEtalon implements MathFunction {

    private final double base;

    public LogEtalon(double base) {
        this.base = base;
    }

    @Override
    public double apply(double x) {
        if (Math.abs(base - 2) < 1e-12) {
            if (Math.abs(x - 0.5) < 1e-12) return -1;
            if (Math.abs(x - 1) < 1e-12) return 0;
            if (Math.abs(x - 2) < 1e-12) return 1;
            if (Math.abs(x - 4) < 1e-12) return 2;
            if (Math.abs(x - 5) < 1e-12) return 2.321928094887362;
            if (Math.abs(x - 8) < 1e-12) return 3;
            if (Math.abs(x - 10) < 1e-12) return 3.321928094887362;
        } else if (Math.abs(base - 5) < 1e-12) {
            if (Math.abs(x - 0.5) < 1e-12) return -0.4306765580733;
            if (Math.abs(x - 1) < 1e-12) return 0;
            if (Math.abs(x - 2) < 1e-12) return 0.4306765580733;
            if (Math.abs(x - 4) < 1e-12) return 0.861353116146786;
            if (Math.abs(x - 5) < 1e-12) return 1;
            if (Math.abs(x - 8) < 1e-12) return 1.292029674220;
            if (Math.abs(x - 10) < 1e-12) return 1.430676558073;
        } else if (Math.abs(base - 10.0) < 1e-12) {
            if (Math.abs(x - 0.5) < 1e-12) return -0.3010299956639812;
            if (Math.abs(x - 1) < 1e-12) return 0;
            if (Math.abs(x - 2) < 1e-12) return 0.3010299956639812;
            if (Math.abs(x - 4) < 1e-12) return 0.6020599913279624;
            if (Math.abs(x - 5) < 1e-12) return 0.6989700043360189;
            if (Math.abs(x - 8) < 1e-12) return 0.9030899869919435;
            if (Math.abs(x - 10) < 1e-12) return 1;
        }
        throw new UnsupportedOperationException("Etalon log не поддерживает (base=" + base + ", x=" + x + ")");
    }
}