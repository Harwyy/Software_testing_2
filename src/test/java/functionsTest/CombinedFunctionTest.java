package functionsTest;

import functions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class CombinedFunctionTest {
    private static final double EPS = 1e-12;

    private final LnSeries ln = new LnSeries(EPS);
    private final SinSeries sin = new SinSeries(EPS);
    private final CosSeries cos = new CosSeries(sin);
    private final TanSeries tan = new TanSeries(sin, cos);
    private final Log2Series log2 = new Log2Series(ln);
    private final Log5Series log5 = new Log5Series(ln);
    private final Log10Series log10 = new Log10Series(ln);

    private final NegativeXFunction negativeFunc = new NegativeXFunction(cos, tan);
    private final PositiveXFunction positiveFunc = new PositiveXFunction(log2, log5, log10);
    private final CombinedFunction combined = new CombinedFunction(negativeFunc, positiveFunc);

    @Test
    void testComputeAtZero() {
        assertEquals(1, combined.compute(0), EPS);
    }

    @Test
    void testThrowsAtNegativePiHalf() {
        assertThrows(ArithmeticException.class, () -> combined.compute(-Math.PI / 2));
    }

    @ParameterizedTest
    @CsvSource({
            "-6.283185307179586, 1",
            "-3.141592653589793, -1",
            "-2, -2.6011866998086615",
            "-1.5, 14.172157148839421",
            "-1, 2.097710030523",
            "-0.5, 1.4238850517341632"
    })
    void testCombinedWithNegativeX(double x, double expected) {
        double actual = combined.compute(x);
        assertEquals(expected, actual, 1e-12);
    }

    @ParameterizedTest
    @CsvSource({
            "0.1, -4068.888927003985",
            "0.5, -10.058302583004862",
            "1, NaN",
            "1.5, 0.6889137104957759",
            "2, 10.058302583004862"
    })
    void testCombinedWithPositiveX(double x, double expected) {
        if (Double.isNaN(expected)) {
            assertTrue(Double.isNaN(combined.compute(x)));
        } else {
            double actual = combined.compute(x);
            assertEquals(expected, actual, 1e-7);
        }
    }

    private double computePositiveExpected(double x) {
        double log5Val = Math.log(x * x) / Math.log(5);
        double log10Val = Math.log(x * x * x) / Math.log(10);
        double sum = log5Val + log10Val;
        double square = sum * sum;
        double cube = square * square * square;
        double log2Val = Math.log(x * x * x) / Math.log(2);
        return cube / log2Val;
    }

}