import functions.CosSeries;
import functions.SinSeries;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CosSeriesIntegrationTest {
    private static final double EPS = 1e-15;
    private static final String sinSeriesCsv = "sin_series_mock.csv";
    private static final String sinMathCsv = "sin_math_mock.csv";

    @BeforeAll
    static void generateCsvFiles() throws IOException {
        SinSeries realSin = new SinSeries(EPS);

        double startSin = -4 * Math.PI;
        double endSin = 4 * Math.PI;
        double step = 0.001;
        int steps = (int) Math.round((endSin - startSin) / step);

        List<double[]> sinSeriesData = new ArrayList<>();
        List<double[]> sinMathData = new ArrayList<>();

        for (int i = 0; i <= steps; i++) {
            double x = startSin + i * step;
            sinSeriesData.add(new double[]{x, realSin.sin(x)});
            sinMathData.add(new double[]{x, Math.sin(x)});
        }

        CsvUtils.write(sinSeriesCsv, sinSeriesData.toArray(new double[0][]));
        CsvUtils.write(sinMathCsv, sinMathData.toArray(new double[0][]));
    }

    @AfterAll
    static void cleanup() throws IOException {
        Files.deleteIfExists(Path.of(sinSeriesCsv));
        Files.deleteIfExists(Path.of(sinMathCsv));
    }

    @Test
    void testCosUsingMockSinFromSeries() throws IOException {
        MockFunction mockSinFunc = new MockFunction(sinSeriesCsv);
        SinSeries mockSin = new SinSeries(EPS) {
            @Override
            public double sin(double x) {
                return mockSinFunc.apply(x);
            }
        };
        CosSeries cosSeries = new CosSeries(mockSin);

        double start = -3 * Math.PI;
        double end = 3 * Math.PI;
        double step = 0.3;
        for (double x = start; x <= end; x += step) {
            double expected = Math.cos(x);
            double actual = cosSeries.cos(x);
            assertEquals(expected, actual, 1e-7);
        }
    }

    @Test
    void testCosUsingMockSinFromMath() throws IOException {
        MockFunction mockSinMath = new MockFunction(sinMathCsv);
        SinSeries mockSin = new SinSeries(EPS) {
            @Override
            public double sin(double x) {
                return mockSinMath.apply(x);
            }
        };
        CosSeries cosSeries = new CosSeries(mockSin);

        double start = -3 * Math.PI;
        double end = 3 * Math.PI;
        double step = 0.3;
        for (double x = start; x <= end; x += step) {
            double expected = Math.cos(x);
            double actual = cosSeries.cos(x);
            assertEquals(expected, actual, 1e-7);
        }
    }

    @Test
    void testCosUsingSinSeries() throws IOException {
        SinSeries mockSin = new SinSeries(EPS);
        CosSeries cosSeries = new CosSeries(mockSin);

        double start = -3 * Math.PI;
        double end = 3 * Math.PI;
        double step = 0.3;
        for (double x = start; x <= end; x += step) {
            double expected = Math.cos(x);
            double actual = cosSeries.cos(x);
            assertEquals(expected, actual, 1e-7);
        }
    }
}