package functionsTest;

import functions.LnSeries;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class LnSeriesTest {
    private final LnSeries lnSeries = new LnSeries(1e-12);

    @Test
    void testLnOne() {
        assertEquals(0, lnSeries.ln(1.0), 1e-12);
    }

    @Test
    void testLnE() {
        assertEquals(1, lnSeries.ln(Math.E), 1e-12);
    }

    @Test
    void testLnZeroOrNegativeThrows() {
        assertThrows(IllegalArgumentException.class, () -> lnSeries.ln(0));
        assertThrows(IllegalArgumentException.class, () -> lnSeries.ln(-1));
        assertThrows(IllegalArgumentException.class, () -> lnSeries.ln(-5.5));
    }

    @ParameterizedTest
    @CsvSource({
            "0.1, -2.302585092994046, 1e-12",
            "0.5, -0.6931471805599453, 1e-12",
            "1, 0, 1e-12",
            "1.5, 0.4054651081081644, 1e-12",
            "2, 0.6931471805599453, 1e-12",
            "2.5, 0.9162907318741551, 1e-12",
            "3, 1.0986122886681098, 1e-12",
            "5, 1.6094379124341003, 1e-12",
            "10, 2.302585092994046, 1e-12",
            "100, 4.605170185988092, 1e-12"
    })
    void testLnVsMath(double x, double expected, double eps) {
        double actual = lnSeries.ln(x);
        assertEquals(expected, actual, eps);
    }
}