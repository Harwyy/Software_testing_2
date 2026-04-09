package functionsTest;

import functions.LnSeries;
import functions.Log2Series;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class Log2SeriesTest {
    private final LnSeries ln = new LnSeries(1e-12);
    private final Log2Series log2 = new Log2Series(ln);

    @Test
    void testLog2One() {
        assertEquals(0, log2.log2(1), 1e-12);
    }

    @Test
    void testLog2Two() {
        assertEquals(1, log2.log2(2), 1e-12);
    }

    @Test
    void testLog2Four() {
        assertEquals(2, log2.log2(4), 1e-12);
    }

    @Test
    void testLog2Half() {
        assertEquals(-1, log2.log2(0.5), 1e-12);
    }

    @Test
    void testLog2NegativeThrows() {
        assertThrows(IllegalArgumentException.class, () -> log2.log2(-1));
        assertThrows(IllegalArgumentException.class, () -> log2.log2(-5.5));
    }

    @Test
    void testLog2ZeroThrows() {
        assertThrows(IllegalArgumentException.class, () -> log2.log2(0));
    }

    @ParameterizedTest
    @CsvSource({
            "0.125, -3, 1e-12",
            "0.25, -2, 1e-12",
            "0.5, -1, 1e-12",
            "1, 0, 1e-12",
            "2, 1, 1e-12",
            "4, 2, 1e-12",
            "8, 3, 1e-12",
            "16, 4, 1e-12",
            "32, 5, 1e-12"
    })
    void testLog2VsMath(double x, double expected, double eps) {
        double actual = log2.log2(x);
        assertEquals(expected, actual, eps);
    }
}