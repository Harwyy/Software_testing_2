package functions;

public class Log2Series {
    private final LnSeries ln;
    private final double ln2;

    public Log2Series(LnSeries ln) {
        this.ln = ln;
        this.ln2 = ln.ln(2);
    }

    public double log2(double x) {
        if (x <= 0) {
            throw new IllegalArgumentException("log2(x) defined only for x > 0");
        }
        return ln.ln(x) / ln2;
    }
}