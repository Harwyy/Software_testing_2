package functions;

public class SinFunc implements MathFunction {
    private final double eps;

    public SinFunc(double eps) {
        this.eps = eps;
    }

    @Override
    public double apply(double x) {
        x = x % (2 * Math.PI);
        if (x > Math.PI) x -= 2 * Math.PI;
        if (x < -Math.PI) x += 2 * Math.PI;

        int sign = 1;
        if (x < 0) {
            x = -x;
            sign = -1;
        }
        if (x > Math.PI / 2) {
            x = Math.PI - x;
        }

        double term = x;
        double sum = term;
        int n = 1;
        while (Math.abs(term) > eps) {
            term *= -x * x / ((2 * n) * (2 * n + 1));
            sum += term;
            n++;
        }
        return sign * sum;
    }
}