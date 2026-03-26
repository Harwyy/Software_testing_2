package BaseFunc;

import etalons.SinEtalon;
import functions.MathFunction;
import functions.SinFunc;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SinFuncTest {

    private static final double EPS = 1e-10;
    private static final double DELTA = 1e-8;

    private static final double[] TEST_POINTS = {
            0.0,
            Math.PI / 2,
            Math.PI,
            -Math.PI / 2,
            Math.PI / 4,
            -Math.PI / 4,
            Math.PI / 6,
            Math.PI / 3
    };

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7})
    void sinFuncMatchSinEtalon(int index) {
        MathFunction sinReal = new SinFunc(EPS);
        MathFunction sinEtalon = new SinEtalon();

        double x = TEST_POINTS[index];
        double expected = sinEtalon.apply(x);
        double actual = sinReal.apply(x);
        assertEquals(expected, actual, DELTA);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.9, -0.75, -0.5, -0.25, 0, 0.25, 0.5, 0.75, 0.9})
    void sinFuncMustBeOdd(double x) {
        MathFunction sinReal = new SinFunc(EPS);
        assertEquals(-sinReal.apply(-x), sinReal.apply(x), DELTA);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.9, -0.75, -0.5, -0.25, 0, 0.25, 0.5, 0.75, 0.9})
    void sinFuncMustHavePeriod2P(double x) {
        MathFunction sinReal = new SinFunc(EPS);
        double period = 2 * Math.PI;
        assertEquals(sinReal.apply(x + period), sinReal.apply(x), DELTA);
    }

}
