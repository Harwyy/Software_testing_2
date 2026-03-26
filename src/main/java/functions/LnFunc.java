package functions;

public class LnFunc implements MathFunction {
    private final double eps;
    private final double ln2;

    public LnFunc(double eps) {
        this.eps = eps;
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
            if (Math.abs(term) < eps) break;
            n++;
            power /= 9.0;
        }
        return 2 * sum;
    }

    @Override
    public double apply(double x) {
        if (x <= 0) throw new IllegalArgumentException("ln(x) defined only for x>0");
        int e = 0;
        double m = x;
        while (m >= 2) { m /= 2; e++; }
        while (m < 1)  { m *= 2; e--; }

        double y = m - 1;
        double lnm = 0.0;
        double term = y;
        int n = 1;
        while (Math.abs(term) > eps) {
            lnm += term;
            n++;
            term *= -y * (n - 1) / n;
        }
        return lnm + e * ln2;
    }
}