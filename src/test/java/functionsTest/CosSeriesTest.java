package functionsTest;

import functions.CosSeries;
import functions.SinSeries;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class CosSeriesTest {
    private final CosSeries cosSeries = new CosSeries(new SinSeries(1e-12));

    @Test
    void testCosZero() {
        assertEquals(1, cosSeries.cos(0), 1e-12);
    }

    @Test
    void testCosPi() {
        assertEquals(-1, cosSeries.cos(Math.PI), 1e-12);
    }

    @Test
    void testCosPiHalf() {
        assertEquals(0, cosSeries.cos(Math.PI / 2), 1e-12);
    }

    @ParameterizedTest
    @CsvSource({
            "-6.283185307179586, 1, 1e-12",
            "-3.141592653589793, -1, 1e-12",
            "-1.5707963267948966, 0, 1e-12",
            "-0.5, 0.8775825618903728, 1e-12",
            "0, 1, 1e-12",
            "0.5, 0.8775825618903728, 1e-12",
            "1.0471975511965976, 0.5, 1e-12",
            "1.5707963267948966, 0, 1e-12",
            "3.141592653589793, -1, 1e-12",
            "6.283185307179586, 1, 1e-12"
    })
    void testCosVsMathWithCsv(double x, double expected, double eps) {
        double actual = cosSeries.cos(x);
        assertEquals(expected, actual, eps);
    }
}