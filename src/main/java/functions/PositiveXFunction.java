package functions;

public class PositiveXFunction {
    private final Log2Series log2;
    private final Log5Series log5;
    private final Log10Series log10;

    public PositiveXFunction(Log2Series log2, Log5Series log5, Log10Series log10) {
        this.log2 = log2;
        this.log5 = log5;
        this.log10 = log10;
    }

    public double compute(double x) {
        if (x <= 0) {
            throw new IllegalArgumentException("x > 0 required");
        }
        Double log5Val = log5.log5(x*x);
        Double log10Val = log10.log10(x*x*x);
        Double square = (log5Val + log10Val) * (log5Val + log10Val);
        return (square * square * square) / (log2.log2(x*x*x));
    }
}
