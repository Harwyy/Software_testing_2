package functionsTest;

import functions.LnSeries;
import functions.Log10Series;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class Log10SeriesTest {
    private final LnSeries ln = new LnSeries(1e-8);
    private final Log10Series log10 = new Log10Series(ln);

    @Test
    void testLog10One() {
        assertEquals(0.0, log10.log10(1), 1e-8);
    }

    @Test
    void testLog10Ten() {
        assertEquals(1.0, log10.log10(10), 1e-8);
    }

    @Test
    void testLog10Hundred() {
        assertEquals(2.0, log10.log10(100), 1e-8);
    }

    @Test
    void testLog10OneTenth() {
        assertEquals(-1.0, log10.log10(0.1), 1e-8);
    }

    @Test
    void testLog10NegativeThrows() {
        assertThrows(IllegalArgumentException.class, () -> log10.log10(-1));
        assertThrows(IllegalArgumentException.class, () -> log10.log10(-5));
    }

    @Test
    void testLog10ZeroThrows() {
        assertThrows(IllegalArgumentException.class, () -> log10.log10(0));
    }

    @ParameterizedTest
    @CsvSource({
            "0.001, -3, 1e-8",
            "0.01, -2, 1e-8",
            "0.1, -1, 1e-8",
            "1, 0, 1e-8",
            "10, 1, 1e-8",
            "100, 2, 1e-8",
            "1000, 3, 1e-8",
            "10000, 4, 1e-8",
            "100000, 5, 1e-8"
    })
    void testLog10VsExpected(double x, double expected, double eps) {
        double actual = log10.log10(x);
        assertEquals(expected, actual, eps);
    }
}