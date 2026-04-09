package functions;

public class Log5Series {
    private final LnSeries ln;
    private final double ln5;

    public Log5Series(LnSeries ln) {
        this.ln = ln;
        this.ln5 = ln.ln(5);
    }

    public double log5(double x) {
        if (x <= 0) {
            throw new IllegalArgumentException("log5(x) defined only for x > 0");
        }
        return ln.ln(x) / ln5;
    }
}
