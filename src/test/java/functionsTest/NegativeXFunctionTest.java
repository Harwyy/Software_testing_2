package functionsTest;

import functions.CosSeries;
import functions.NegativeXFunction;
import functions.SinSeries;
import functions.TanSeries;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class NegativeXFunctionTest {
    private final SinSeries sin = new SinSeries(1e-12);
    private final CosSeries cos = new CosSeries(sin);
    private final TanSeries tan = new TanSeries(sin, cos);
    private final NegativeXFunction func = new NegativeXFunction(cos, tan);

    @Test
    void testAtZero() {
        assertEquals(1, func.compute(0), 1e-12);
    }

    @Test
    void testThrowsForPositiveX() {
        assertThrows(IllegalArgumentException.class, () -> func.compute(1));
        assertThrows(IllegalArgumentException.class, () -> func.compute(3.14));
    }

    @Test
    void testThrowsAtNegativePiHalf() {
        assertThrows(ArithmeticException.class, () -> func.compute(-Math.PI / 2));
    }

    @ParameterizedTest
    @CsvSource({
            "-6.283185307179586, 1",
            "-3.141592653589793, -1",
            "-2, -2.6011866998086615",
            "-1.5, 14.172157148839421",
            "-1, 2.097710030523",
            "-0.5, 1.4238850517341632",
            "0, 1"
    })
    void testNegativeXFunctionVsMath(double x, double expected) {
        double actual = func.compute(x);
        assertEquals(expected, actual, 1e-12);
    }
}