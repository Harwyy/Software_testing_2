package functions;

public class TanSeries {
    private final SinSeries sin;
    private final CosSeries cos;

    public TanSeries(SinSeries sin, CosSeries cos) {
        this.sin = sin;
        this.cos = cos;
    }

    public double tan(double x) {
        double sinVal = sin.sin(x);
        double cosVal = cos.cos(x);
        if (Math.abs(cosVal) < 1e-12) {
            throw new ArithmeticException("tan(x) is undefined for cos(x) = 0");
        }
        return sinVal / cosVal;
    }
}