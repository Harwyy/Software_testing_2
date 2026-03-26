package BaseFunc;

import etalons.LnEtalon;
import functions.LnFunc;
import functions.MathFunction;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LnFuncTest {

    private static final double EPS = 1e-10;
    private static final double DELTA = 1e-8;

    private static final double[] TEST_POINTS = {1.0, Math.E, 2.0, 5.0, 10.0};

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4})
    void lnFuncMatchLnEtalon(int index) {
        MathFunction lnReal = new LnFunc(EPS);
        MathFunction lnEtalon = new LnEtalon();

        double x = TEST_POINTS[index];
        double expected = lnEtalon.apply(x);
        double actual = lnReal.apply(x);
        assertEquals(expected, actual, DELTA);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0, -2.5, -Math.PI})
    void lnFuncMustThrowExceptionWithNegativeOrZeroX(double x) {
        MathFunction lnReal = new LnFunc(EPS);
        assertThrows(IllegalArgumentException.class, () -> lnReal.apply(x));
    }

    @ParameterizedTest
    @CsvSource({
            "2.0, 3.0",
            "2.0, 5.0",
            "5.0, 7.0",
            "1.5, 2.0",
            "Math.E, 2.0"
    })
    void lnFuncAddTest(double a, double b) {
        MathFunction lnReal = new LnFunc(EPS);
        double expected = lnReal.apply(a) + lnReal.apply(b);
        double actual = lnReal.apply(a * b);
        assertEquals(expected, actual, DELTA);
    }

}
