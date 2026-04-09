package functions;

public class CombinedFunction {
    private final NegativeXFunction negativeFunc;
    private final PositiveXFunction positiveFunc;

    public CombinedFunction(NegativeXFunction negativeFunc, PositiveXFunction positiveFunc) {
        this.negativeFunc = negativeFunc;
        this.positiveFunc = positiveFunc;
    }

    public double compute(double x) {
        if (x <= 0) {
            return negativeFunc.compute(x);
        } else {
            return positiveFunc.compute(x);
        }
    }
}