package functions;

public class CosSeries {
    private final SinSeries sin;

    public CosSeries(SinSeries sin) {
        this.sin = sin;
    }

    public double cos(double x) {
        return sin.sin(Math.PI / 2 - x);
    }
}