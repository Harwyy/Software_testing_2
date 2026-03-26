package functions;

import etalons.*;

public class FuncFactory {
    private final double eps;
    private final boolean useStubs;

    public FuncFactory(double eps) {
        this(eps, false);
    }

    public FuncFactory(double eps, boolean useStubs) {
        this.eps = eps;
        this.useStubs = useStubs;
    }

    public MathFunction createSin() {
        if (useStubs) {
            return new SinEtalon();
        } else {
            return new SinFunc(eps);
        }
    }

    public MathFunction createLn() {
        if (useStubs) {
            return new LnEtalon();
        } else {
            return new LnFunc(eps);
        }
    }

    public MathFunction createCos() {
        if (useStubs) {
            return new CosEtalon();
        } else {
            return new CosFunc(createSin());
        }
    }

    public MathFunction createTan() {
        if (useStubs) {
            return new TanEtalon();
        } else {
            return new TanFunc(createSin(), createCos());
        }
    }

    public MathFunction createLog(double base) {
        if (useStubs) {
            return new LogEtalon(base);
        } else {
            return new LogFunc(base, createLn());
        }
    }

    public MathFunction createLog2() { return createLog(2); }
    public MathFunction createLog5() { return createLog(5); }
    public MathFunction createLog10() { return createLog(10); }

    public MathFunction createPiecewise() {
        return new Func(createCos(), createTan(), createLog2(), createLog5(), createLog10());
    }
}