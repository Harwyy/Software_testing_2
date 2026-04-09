package functionsTest;

import functions.CosSeries;
import functions.SinSeries;
import functions.TanSeries;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class TanSeriesTest {
    private final SinSeries sin = new SinSeries(1e-12);
    private final CosSeries cos = new CosSeries(sin);
    private final TanSeries tanSeries = new TanSeries(sin, cos);

    @Test
    void testTanZero() {
        assertEquals(0, tanSeries.tan(0), 1e-12);
    }

    @Test
    void testTanPiFourth() {
        assertEquals(1, tanSeries.tan(Math.PI / 4), 1e-12);
    }

    @Test
    void testTanMinusPiFourth() {
        assertEquals(-1, tanSeries.tan(-Math.PI / 4), 1e-12);
    }

    @Test
    void testTanPi() {
        assertEquals(0, tanSeries.tan(Math.PI), 1e-12);
    }

    @ParameterizedTest
    @CsvSource({
            "-6.283185307179586, 0, 1e-12",
            "-3.141592653589793, 0, 1e-12",
            "-1.0471975511965976, -1.7320508075688772, 1e-12",
            "-0.7853981633974483, -1, 1e-12",
            "-0.5235987755982988, -0.5773502691896257, 1e-12",
            "0, 0, 1e-12",
            "0.5235987755982988, 0.5773502691896257, 1e-12",
            "0.7853981633974483, 1, 1e-12",
            "1.0471975511965976, 1.7320508075688772, 1e-12",
            "3.141592653589793, 0, 1e-12",
            "6.283185307179586, 0, 1e-12"
    })
    void testTanVsMathWithCsv(double x, double expected, double eps) {
        double actual = tanSeries.tan(x);
        assertEquals(expected, actual, eps);
    }
}