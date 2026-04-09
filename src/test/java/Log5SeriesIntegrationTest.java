import functions.LnSeries;
import functions.Log5Series;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Log5SeriesIntegrationTest {
    private static final double EPS = 1e-9;
    private static final String lnSeriesCsv = "ln_series_mock5.csv";
    private static final String lnMathCsv = "ln_math_mock5.csv";

    @BeforeAll
    static void generateCsvFiles() throws IOException {
        LnSeries realLn = new LnSeries(EPS);

        double startLn = 0.1;
        double endLn = 10;
        double step = 0.1;
        int steps = (int) Math.round((endLn - startLn) / step);

        List<double[]> lnSeriesData = new ArrayList<>();
        List<double[]> lnMathData = new ArrayList<>();

        for (int i = 0; i <= steps; i++) {
            double x = startLn + i * step;
            lnSeriesData.add(new double[]{x, realLn.ln(x)});
            lnMathData.add(new double[]{x, Math.log(x)});
        }

        CsvUtils.write(lnSeriesCsv, lnSeriesData.toArray(new double[0][]));
        CsvUtils.write(lnMathCsv, lnMathData.toArray(new double[0][]));
    }

    @AfterAll
    static void cleanup() throws IOException {
        Files.deleteIfExists(Path.of(lnSeriesCsv));
        Files.deleteIfExists(Path.of(lnMathCsv));
    }

    @Test
    void testLog5UsingMockLnFromSeries() throws IOException {
        MockFunction mockLnFunc = new MockFunction(lnSeriesCsv);
        LnSeries mockLn = new LnSeries(EPS) {
            @Override
            public double ln(double x) {
                return mockLnFunc.apply(x);
            }
        };
        Log5Series log5 = new Log5Series(mockLn);

        double start = 0.2;
        double end = 8;
        double step = 0.3;
        for (double x = start; x <= end + 1e-12; x += step) {
            double expected = Math.log(x) / Math.log(5);
            double actual = log5.log5(x);
            assertEquals(expected, actual, 1e-7);
        }
    }

    @Test
    void testLog5UsingMockLnFromMath() throws IOException {
        MockFunction mockLnMath = new MockFunction(lnMathCsv);
        LnSeries mockLn = new LnSeries(EPS) {
            @Override
            public double ln(double x) {
                return mockLnMath.apply(x);
            }
        };
        Log5Series log5 = new Log5Series(mockLn);

        double start = 0.2;
        double end = 8;
        double step = 0.3;
        for (double x = start; x <= end + 1e-12; x += step) {
            double expected = Math.log(x) / Math.log(5);
            double actual = log5.log5(x);
            assertEquals(expected, actual, 1e-7);
        }
    }

    @Test
    void testLog5UsingRealLn() {
        LnSeries realLn = new LnSeries(EPS);
        Log5Series log5 = new Log5Series(realLn);

        double start = 0.2;
        double end = 8;
        double step = 0.3;
        for (double x = start; x <= end + 1e-12; x += step) {
            double expected = Math.log(x) / Math.log(5);
            double actual = log5.log5(x);
            assertEquals(expected, actual, 1e-7);
        }
    }
}