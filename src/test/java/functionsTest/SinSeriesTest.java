package functionsTest;

import functions.SinSeries;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class SinSeriesTest {
    private final SinSeries sinSeries = new SinSeries(1e-12);;

    @Test
    void testSinZero() {
        assertEquals(0, sinSeries.sin(0), 1e-12);
    }

    @Test
    void testSinPi() {
        assertEquals(0, sinSeries.sin(Math.PI), 1e-12);
    }

    @ParameterizedTest
    @CsvSource({
            "-6.283185307179586, 0, 1e-12",
            "-3.141592653589793, 0, 1e-12",
            "-1.5707963267948966, -1, 1e-12",
            "-0.5, -0.4794255386042, 1e-12",
            "0, 0, 1e-12",
            "0.5, 0.4794255386042, 1e-12",
            "1.0471975511965976, 0.8660254037844386, 1e-12",
            "1.5707963267948966, 1, 1e-12",
            "3.141592653589793, 0, 1e-12",
            "6.283185307179586, 0, 1e-12"
    })
    void testSinVsMathWithCsv(double x, double expected, double eps) {
        double our = sinSeries.sin(x);
        assertEquals(expected, our, eps);
    }
}