package functions;

public class NegativeXFunction {
    private final CosSeries cos;
    private final TanSeries tan;

    public NegativeXFunction(CosSeries cos, TanSeries tan) {
        this.cos = cos;
        this.tan = tan;
    }

    public double compute(double x) {
        if (x > 0) {
            throw new IllegalArgumentException("x <= 0");
        }
        double cosVal = cos.cos(x);
        double tanVal = tan.tan(x);
        return cosVal - tanVal;
    }
}