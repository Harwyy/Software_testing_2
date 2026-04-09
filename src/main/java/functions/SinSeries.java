package functions;

public class SinSeries {
    private final double eps;

    public SinSeries(double epsilon) {
        this.eps = epsilon;
    }

    public double sin(double x) {
        double twoPi = 2 * Math.PI;
        x = x % twoPi;
        if (x > Math.PI) x -= twoPi;
        if (x < -Math.PI) x += twoPi;

        double term = x;
        double sum = term;
        int n = 1;
        while (Math.abs(term) > eps) {
            term *= -x * x / ((2 * n) * (2 * n + 1));
            sum += term;
            n++;
        }
        return sum;
    }
}