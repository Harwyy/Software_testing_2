package functions;

public class Func implements MathFunction {
    private final MathFunction cos;
    private final MathFunction tan;
    private final MathFunction log2;
    private final MathFunction log5;
    private final MathFunction log10;

    public Func(MathFunction cos, MathFunction tan,
                MathFunction log2, MathFunction log5, MathFunction log10) {
        this.cos = cos;
        this.tan = tan;
        this.log2 = log2;
        this.log5 = log5;
        this.log10 = log10;
    }

    @Override
    public double apply(double x) {
        if (x <= 0) {
            return cos.apply(x) - tan.apply(x);
        } else {
            if (Math.abs(x - 1.0) < 1e-12) {
                throw new ArithmeticException("f(x) undefined at x=1");
            }
            double log5x = log5.apply(x);
            double log10x = log10.apply(x);
            double numerator = Math.pow(log5x * log5x - log10x * log10x * log10x, 6);
            double denominator = Math.pow(log2.apply(x), 3);
            return numerator / denominator;
        }
    }
}