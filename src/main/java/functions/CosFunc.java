package functions;

public class CosFunc implements MathFunction {
    private final MathFunction sin;

    public CosFunc(MathFunction sin) {
        this.sin = sin;
    }

    @Override
    public double apply(double x) {
        return sin.apply(x + Math.PI / 2);
    }
}