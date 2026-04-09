package functionsTest;

import functions.LnSeries;
import functions.Log5Series;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class Log5SeriesTest {
    private final LnSeries ln = new LnSeries(1e-8);
    private final Log5Series log5 = new Log5Series(ln);

    @Test
    void testLog5One() {
        assertEquals(0.0, log5.log5(1), 1e-8);
    }

    @Test
    void testLog5Five() {
        assertEquals(1.0, log5.log5(5), 1e-8);
    }

    @Test
    void testLog5TwentyFive() {
        assertEquals(2.0, log5.log5(25), 1e-8);
    }

    @Test
    void testLog5OneFifth() {
        assertEquals(-1.0, log5.log5(0.2), 1e-8);
    }

    @Test
    void testLog5NegativeThrows() {
        assertThrows(IllegalArgumentException.class, () -> log5.log5(-1));
        assertThrows(IllegalArgumentException.class, () -> log5.log5(-5.5));
    }

    @Test
    void testLog5ZeroThrows() {
        assertThrows(IllegalArgumentException.class, () -> log5.log5(0));
    }

    @ParameterizedTest
    @CsvSource({
            "0.008, -3, 1e-8",
            "0.04, -2, 1e-8",
            "0.2, -1, 1e-8",
            "1, 0, 1e-8",
            "5, 1, 1e-8",
            "25, 2, 1e-8",
            "125, 3, 1e-8",
            "3125, 5, 1e-8"
    })
    void testLog5VsExpected(double x, double expected, double eps) {
        double actual = log5.log5(x);
        assertEquals(expected, actual, eps);
    }
}