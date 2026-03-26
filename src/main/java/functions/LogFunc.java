package functions;

public class LogFunc implements MathFunction {
    private final double base;
    private final MathFunction ln;

    public LogFunc(double base, MathFunction ln) {
        if (base <= 0 || base == 1) throw new IllegalArgumentException("Invalid base");
        this.base = base;
        this.ln = ln;
    }

    @Override
    public double apply(double x) {
        if (x <= 0) throw new IllegalArgumentException("log(x) defined only for x>0");
        return ln.apply(x) / ln.apply(base);
    }
}