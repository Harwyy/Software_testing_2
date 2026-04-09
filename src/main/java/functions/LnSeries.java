package functions;

public class LnSeries {
    private final double eps;
    private final double ln2;

    public LnSeries(double epsilon) {
        this.eps = epsilon;
        this.ln2 = computeLn2();
    }

    private double computeLn2() {
        double sum = 0.0;
        double term;
        int n = 1;
        double power = 1.0 / 3.0;
        while (true) {
            term = power / (2 * n - 1);
            sum += term;
            if (term < eps && term > -eps) break;
            n++;
            power /= 9.0;
        }
        return 2 * sum;
    }

    public double ln(double x) {
        if (x <= 0) {
            throw new IllegalArgumentException("ln(x) is only defined for x > 0");
        }
        int e = 0;
        double m = x;
        while (m >= 2) {
            m /= 2;
            e++;
        }
        while (m < 1) {
            m *= 2;
            e--;
        }
        double y = m - 1;
        double term = y;
        double result = 0.0;
        int n = 1;
        while (Math.abs(term) > eps) {
            result += term;
            n++;
            term *= -y * (n - 1) / n;
        }
        return result + e * ln2;
    }
}