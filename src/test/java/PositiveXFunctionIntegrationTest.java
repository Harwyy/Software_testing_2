import functions.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PositiveXFunctionIntegrationTest {
    private static final double EPS = 1e-12;
    private static final String log2SeriesCsv = "log2_series_mock.csv";
    private static final String log2MathCsv = "log2_math_mock.csv";
    private static final String log5SeriesCsv = "log5_series_mock.csv";
    private static final String log5MathCsv = "log5_math_mock.csv";
    private static final String log10SeriesCsv = "log10_series_mock.csv";
    private static final String log10MathCsv = "log10_math_mock.csv";

    @BeforeAll
    static void generateCsvFiles() throws IOException {
        LnSeries realLn = new LnSeries(EPS);
        Log2Series realLog2 = new Log2Series(realLn);
        Log5Series realLog5 = new Log5Series(realLn);
        Log10Series realLog10 = new Log10Series(realLn);

        double start = 0.001;
        double end = 10;
        double step = 0.001;
        int steps = (int) Math.round((end - start) / step);

        List<double[]> log2SeriesData = new ArrayList<>();
        List<double[]> log2MathData = new ArrayList<>();
        List<double[]> log5SeriesData = new ArrayList<>();
        List<double[]> log5MathData = new ArrayList<>();
        List<double[]> log10SeriesData = new ArrayList<>();
        List<double[]> log10MathData = new ArrayList<>();

        for (int i = 0; i <= steps; i++) {
            double x = start + i * step;
            log2SeriesData.add(new double[]{x, realLog2.log2(x)});
            log2MathData.add(new double[]{x, Math.log(x) / Math.log(2)});
            log5SeriesData.add(new double[]{x, realLog5.log5(x)});
            log5MathData.add(new double[]{x, Math.log(x) / Math.log(5)});
            log10SeriesData.add(new double[]{x, realLog10.log10(x)});
            log10MathData.add(new double[]{x, Math.log10(x)});
        }

        CsvUtils.write(log2SeriesCsv, log2SeriesData.toArray(new double[0][]));
        CsvUtils.write(log2MathCsv, log2MathData.toArray(new double[0][]));
        CsvUtils.write(log5SeriesCsv, log5SeriesData.toArray(new double[0][]));
        CsvUtils.write(log5MathCsv, log5MathData.toArray(new double[0][]));
        CsvUtils.write(log10SeriesCsv, log10SeriesData.toArray(new double[0][]));
        CsvUtils.write(log10MathCsv, log10MathData.toArray(new double[0][]));
    }

    @AfterAll
    static void cleanup() throws IOException {
        Files.deleteIfExists(Path.of(log2SeriesCsv));
        Files.deleteIfExists(Path.of(log2MathCsv));
        Files.deleteIfExists(Path.of(log5SeriesCsv));
        Files.deleteIfExists(Path.of(log5MathCsv));
        Files.deleteIfExists(Path.of(log10SeriesCsv));
        Files.deleteIfExists(Path.of(log10MathCsv));
    }

    @Test
    void testPositiveXFunctionUsingMocksFromSeries() throws IOException {
        MockFunction mockLog2 = new MockFunction(log2SeriesCsv);
        Log2Series log2Mock = new Log2Series(new LnSeries(EPS)) {
            @Override
            public double log2(double x) {
                return mockLog2.apply(x);
            }
        };

        MockFunction mockLog5 = new MockFunction(log5SeriesCsv);
        Log5Series log5Mock = new Log5Series(new LnSeries(EPS)) {
            @Override
            public double log5(double x) {
                return mockLog5.apply(x);
            }
        };

        MockFunction mockLog10 = new MockFunction(log10SeriesCsv);
        Log10Series log10Mock = new Log10Series(new LnSeries(EPS)) {
            @Override
            public double log10(double x) {
                return mockLog10.apply(x);
            }
        };

        PositiveXFunction func = new PositiveXFunction(log2Mock, log5Mock, log10Mock);

        double start = 0.2;
        double end = 2;
        double step = 0.3;
        for (double x = start; x <= end + 1e-12; x += step) {
            double expected = computeExpected(x);
            double actual = func.compute(x);
            assertEquals(expected, actual, 1e-3);
        }
    }

    @Test
    void testPositiveXFunctionUsingMocksFromMath() throws IOException {
        MockFunction mockLog2 = new MockFunction(log2MathCsv);
        Log2Series log2Mock = new Log2Series(new LnSeries(EPS)) {
            @Override
            public double log2(double x) {
                return mockLog2.apply(x);
            }
        };

        MockFunction mockLog5 = new MockFunction(log5MathCsv);
        Log5Series log5Mock = new Log5Series(new LnSeries(EPS)) {
            @Override
            public double log5(double x) {
                return mockLog5.apply(x);
            }
        };

        MockFunction mockLog10 = new MockFunction(log10MathCsv);
        Log10Series log10Mock = new Log10Series(new LnSeries(EPS)) {
            @Override
            public double log10(double x) {
                return mockLog10.apply(x);
            }
        };

        PositiveXFunction func = new PositiveXFunction(log2Mock, log5Mock, log10Mock);

        double start = 0.2;
        double end = 2;
        double step = 0.3;
        for (double x = start; x <= end + 1e-12; x += step) {
            double expected = computeExpected(x);
            double actual = func.compute(x);
            assertEquals(expected, actual, 1e-3);
        }
    }

    @Test
    void testPositiveXFunctionUsingRealSeries() {
        LnSeries realLn = new LnSeries(EPS);
        Log2Series realLog2 = new Log2Series(realLn);
        Log5Series realLog5 = new Log5Series(realLn);
        Log10Series realLog10 = new Log10Series(realLn);
        PositiveXFunction func = new PositiveXFunction(realLog2, realLog5, realLog10);

        double start = 0.2;
        double end = 7;
        double step = 0.3;
        for (double x = start; x <= end + 1e-12; x += step) {
            double expected = computeExpected(x);
            double actual = func.compute(x);
            assertEquals(expected, actual, 1e-7);
        }
    }

    private double computeExpected(double x) {
        double log5Val = Math.log(x * x) / Math.log(5);
        double log10Val = Math.log(x * x * x) / Math.log(10);
        double sum = log5Val + log10Val;
        double squared = sum * sum;
        double cubed = squared * squared * squared;
        double log2Val = Math.log(x * x * x) / Math.log(2);
        return cubed / log2Val;
    }
}