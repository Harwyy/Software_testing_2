import functions.CosSeries;
import functions.SinSeries;
import functions.TanSeries;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TanSeriesIntegrationTest {
    private static final double EPS = 1e-15;
    private static final String sinSeriesCsv = "sin_series_mock.csv";
    private static final String sinMathCsv = "sin_math_mock.csv";
    private static final String cosSeriesCsv = "cos_series_mock.csv";
    private static final String cosMathCsv = "cos_math_mock.csv";

    @BeforeAll
    static void generateCsvFiles() throws IOException {
        SinSeries realSin = new SinSeries(EPS);
        CosSeries realCos = new CosSeries(realSin);

        double start = -4 * Math.PI;
        double end = 4 * Math.PI;
        double step = 0.001;
        int steps = (int) Math.round((end - start) / step);

        List<double[]> sinSeriesData = new ArrayList<>();
        List<double[]> sinMathData = new ArrayList<>();
        List<double[]> cosSeriesData = new ArrayList<>();
        List<double[]> cosMathData = new ArrayList<>();

        for (int i = 0; i <= steps; i++) {
            double x = start + i * step;
            sinSeriesData.add(new double[]{x, realSin.sin(x)});
            sinMathData.add(new double[]{x, Math.sin(x)});
            cosSeriesData.add(new double[]{x, realCos.cos(x)});
            cosMathData.add(new double[]{x, Math.cos(x)});
        }

        CsvUtils.write(sinSeriesCsv, sinSeriesData.toArray(new double[0][]));
        CsvUtils.write(sinMathCsv, sinMathData.toArray(new double[0][]));
        CsvUtils.write(cosSeriesCsv, cosSeriesData.toArray(new double[0][]));
        CsvUtils.write(cosMathCsv, cosMathData.toArray(new double[0][]));
    }

    @AfterAll
    static void cleanup() throws IOException {
        Files.deleteIfExists(Path.of(sinSeriesCsv));
        Files.deleteIfExists(Path.of(sinMathCsv));
        Files.deleteIfExists(Path.of(cosSeriesCsv));
        Files.deleteIfExists(Path.of(cosMathCsv));
    }

    @Test
    void testTanUsingMocksFromSeries() throws IOException {
        MockFunction mockSin = new MockFunction(sinSeriesCsv);
        MockFunction mockCos = new MockFunction(cosSeriesCsv);
        SinSeries sinMock = new SinSeries(EPS) {
            @Override
            public double sin(double x) { return mockSin.apply(x); }
        };
        CosSeries cosMock = new CosSeries(sinMock) {
            @Override
            public double cos(double x) { return mockCos.apply(x); }
        };
        TanSeries tanSeries = new TanSeries(sinMock, cosMock);

        double start = -3 * Math.PI;
        double end = 3 * Math.PI;
        double step = 0.3;
        for (double x = start; x <= end + 1e-12; x += step) {
            if (Math.abs(Math.cos(x)) < 1e-10) continue;
            double expected = Math.tan(x);
            double actual = tanSeries.tan(x);
            assertEquals(expected, actual, 1e-5);
        }
    }

    @Test
    void testTanUsingMocksFromMath() throws IOException {
        MockFunction mockSin = new MockFunction(sinMathCsv);
        MockFunction mockCos = new MockFunction(cosMathCsv);
        SinSeries sinMock = new SinSeries(EPS) {
            @Override
            public double sin(double x) { return mockSin.apply(x); }
        };
        CosSeries cosMock = new CosSeries(sinMock) {
            @Override
            public double cos(double x) { return mockCos.apply(x); }
        };
        TanSeries tanSeries = new TanSeries(sinMock, cosMock);

        double start = -3 * Math.PI;
        double end = 3 * Math.PI;
        double step = 0.3;
        for (double x = start; x <= end + 1e-12; x += step) {
            if (Math.abs(Math.cos(x)) < 1e-10) continue;
            double expected = Math.tan(x);
            double actual = tanSeries.tan(x);
            assertEquals(expected, actual, 1e-5);
        }
    }

    @Test
    void testTanUsingRealSeries() {
        SinSeries realSin = new SinSeries(EPS);
        CosSeries realCos = new CosSeries(realSin);
        TanSeries tanSeries = new TanSeries(realSin, realCos);

        double start = -3 * Math.PI;
        double end = 3 * Math.PI;
        double step = 0.3;
        for (double x = start; x <= end + 1e-12; x += step) {
            if (Math.abs(Math.cos(x)) < 1e-10) continue;
            double expected = Math.tan(x);
            double actual = tanSeries.tan(x);
            assertEquals(expected, actual, 1e-5);
        }
    }
}