import functions.LnSeries;
import functions.Log10Series;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Log10SeriesIntegrationTest {
    private static final double EPS = 1e-9;
    private static final String lnSeriesCsv = "ln_series_mock10.csv";
    private static final String lnMathCsv = "ln_math_mock10.csv";

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
    void testLog10UsingMockLnFromSeries() throws IOException {
        MockFunction mockLnFunc = new MockFunction(lnSeriesCsv);
        LnSeries mockLn = new LnSeries(EPS) {
            @Override
            public double ln(double x) {
                return mockLnFunc.apply(x);
            }
        };
        Log10Series log10 = new Log10Series(mockLn);

        double start = 0.2;
        double end = 8;
        double step = 0.3;
        for (double x = start; x <= end + 1e-12; x += step) {
            double expected = Math.log(x) / Math.log(10);
            double actual = log10.log10(x);
            assertEquals(expected, actual, 1e-7);
        }
    }

    @Test
    void testLog10UsingMockLnFromMath() throws IOException {
        MockFunction mockLnMath = new MockFunction(lnMathCsv);
        LnSeries mockLn = new LnSeries(EPS) {
            @Override
            public double ln(double x) {
                return mockLnMath.apply(x);
            }
        };
        Log10Series log10 = new Log10Series(mockLn);

        double start = 0.2;
        double end = 8;
        double step = 0.3;
        for (double x = start; x <= end + 1e-12; x += step) {
            double expected = Math.log(x) / Math.log(10);
            double actual = log10.log10(x);
            assertEquals(expected, actual, 1e-7);
        }
    }

    @Test
    void testLog10UsingRealLn() {
        LnSeries realLn = new LnSeries(EPS);
        Log10Series log10 = new Log10Series(realLn);

        double start = 0.2;
        double end = 8;
        double step = 0.3;
        for (double x = start; x <= end + 1e-12; x += step) {
            double expected = Math.log(x) / Math.log(10);
            double actual = log10.log10(x);
            assertEquals(expected, actual, 1e-7);
        }
    }
}