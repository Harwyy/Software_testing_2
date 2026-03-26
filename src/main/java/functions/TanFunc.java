package functions;

public class TanFunc implements MathFunction {
    private final MathFunction sin;
    private final MathFunction cos;

    public TanFunc(MathFunction sin, MathFunction cos) {
        this.sin = sin;
        this.cos = cos;
    }

    @Override
    public double apply(double x) {
        double c = cos.apply(x);
        if (Math.abs(c) < 1e-12) throw new ArithmeticException("tan undefined");
        return sin.apply(x) / c;
    }
}