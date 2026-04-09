package functions;

public class Log10Series {
    private final LnSeries ln;
    private final double ln10;

    public Log10Series(LnSeries ln) {
        this.ln = ln;
        this.ln10 = ln.ln(10);
    }

    public double log10(double x) {
        if (x <= 0) {
            throw new IllegalArgumentException("log10(x) defined only for x > 0");
        }
        return ln.ln(x) / ln10;
    }
}
