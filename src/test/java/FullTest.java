import functions.FuncFactory;
import functions.MathFunction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FullTest {

    private static final double EPS = 1e-10;
    private static final double DELTA = 1e-8;

    @ParameterizedTest
    @CsvSource({
            "-0.7853981633974483, 1.70710678",
            "-0.5235987755982988, 1.44337567",
            "-1.0471975511965976, 2.23205081",
            "0.0, 1.0",
            "0.5, -0.000092759",
            "2.0, 0.00001568",
            "5.0, 0.00651381",
            "10.0, 0.03590043856361"
    })
    void fullFunctionShouldMatchEtalonValues(double x, double expected) {
        FuncFactory factory = new FuncFactory(EPS, false);
        MathFunction f = factory.createPiecewise();
        assertEquals(expected, f.apply(x), DELTA);
    }

    @Test
    void xEqualsOneShouldThrowException() {
        FuncFactory factory = new FuncFactory(EPS, false);
        MathFunction f = factory.createPiecewise();
        assertThrows(ArithmeticException.class, () -> f.apply(1.0));
    }

    @ParameterizedTest
    @CsvSource({
            "-1.5707963267948966",
            "-4.71238898038469",
            "-7.853981633974483",
            "-10.995574287564276"
    })
    void tanSingularitiesShouldThrowException(double x) {
        FuncFactory factory = new FuncFactory(EPS, false);
        MathFunction f = factory.createPiecewise();
        assertThrows(ArithmeticException.class, () -> f.apply(x));
    }

}
