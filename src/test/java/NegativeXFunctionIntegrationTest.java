import functions.CosSeries;
import functions.NegativeXFunction;
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

class NegativeXFunctionIntegrationTest {
    private static final double EPS = 1e-15;
    private static final String cosSeriesCsv = "cos_series_mock.csv";
    private static final String cosMathCsv = "cos_math_mock.csv";
    private static final String tanSeriesCsv = "tan_series_mock.csv";
    private static final String tanMathCsv = "tan_math_mock.csv";

    @BeforeAll
    static void generateCsvFiles() throws IOException {
        SinSeries realSin = new SinSeries(EPS);
        CosSeries realCos = new CosSeries(realSin);
        TanSeries realTan = new TanSeries(realSin, realCos);

        double start = -4 * Math.PI;
        double end = 4 * Math.PI;
        double step = 0.0001;
        int steps = (int) Math.round((end - start) / step);

        List<double[]> cosSeriesData = new ArrayList<>();
        List<double[]> cosMathData = new ArrayList<>();
        List<double[]> tanSeriesData = new ArrayList<>();
        List<double[]> tanMathData = new ArrayList<>();

        for (int i = 0; i <= steps; i++) {
            double x = start + i * step;
            cosSeriesData.add(new double[]{x, realCos.cos(x)});
            cosMathData.add(new double[]{x, Math.cos(x)});
            if (Math.abs(Math.cos(x)) > 1e-12) {
                tanSeriesData.add(new double[]{x, realTan.tan(x)});
                tanMathData.add(new double[]{x, Math.tan(x)});
            }
        }

        CsvUtils.write(cosSeriesCsv, cosSeriesData.toArray(new double[0][]));
        CsvUtils.write(cosMathCsv, cosMathData.toArray(new double[0][]));
        CsvUtils.write(tanSeriesCsv, tanSeriesData.toArray(new double[0][]));
        CsvUtils.write(tanMathCsv, tanMathData.toArray(new double[0][]));
    }

    @AfterAll
    static void cleanup() throws IOException {
        Files.deleteIfExists(Path.of(cosSeriesCsv));
        Files.deleteIfExists(Path.of(cosMathCsv));
        Files.deleteIfExists(Path.of(tanSeriesCsv));
        Files.deleteIfExists(Path.of(tanMathCsv));
    }

    @Test
    void testNegativeXFunctionUsingMocksFromSeries() throws IOException {
        MockFunction mockCos = new MockFunction(cosSeriesCsv);
        CosSeries cosMock = new CosSeries(null) {
            @Override
            public double cos(double x) {
                return mockCos.apply(x);
            }
        };

        MockFunction mockTan = new MockFunction(tanSeriesCsv);
        TanSeries tanMock = new TanSeries(null, null) {
            @Override
            public double tan(double x) {
                return mockTan.apply(x);
            }
        };

        NegativeXFunction func = new NegativeXFunction(cosMock, tanMock);

        double start = -3 * Math.PI;
        double end = 0;
        double step = 0.3;
        for (double x = start; x <= end + 1e-12; x += step) {
            if (Math.abs(Math.cos(x)) < 1e-10) continue;
            double expected = Math.cos(x) - Math.tan(x);
            double actual = func.compute(x);
            assertEquals(expected, actual, 1e-3);
        }
    }

    @Test
    void testNegativeXFunctionUsingMocksFromMath() throws IOException {
        MockFunction mockCos = new MockFunction(cosMathCsv);
        CosSeries cosMock = new CosSeries(null) {
            @Override
            public double cos(double x) {
                return mockCos.apply(x);
            }
        };

        MockFunction mockTan = new MockFunction(tanMathCsv);
        TanSeries tanMock = new TanSeries(null, null) {
            @Override
            public double tan(double x) {
                return mockTan.apply(x);
            }
        };

        NegativeXFunction func = new NegativeXFunction(cosMock, tanMock);

        double start = -3 * Math.PI;
        double end = 0;
        double step = 0.3;
        for (double x = start; x <= end + 1e-12; x += step) {
            if (Math.abs(Math.cos(x)) < 1e-10) continue;
            double expected = Math.cos(x) - Math.tan(x);
            double actual = func.compute(x);
            assertEquals(expected, actual, 1e-3);
        }
    }

    @Test
    void testNegativeXFunctionUsingRealSeries() {
        SinSeries realSin = new SinSeries(EPS);
        CosSeries realCos = new CosSeries(realSin);
        TanSeries realTan = new TanSeries(realSin, realCos);
        NegativeXFunction func = new NegativeXFunction(realCos, realTan);

        double start = -3 * Math.PI;
        double end = 0;
        double step = 0.3;
        for (double x = start; x <= end + 1e-12; x += step) {
            if (Math.abs(Math.cos(x)) < 1e-10) continue;
            double expected = Math.cos(x) - Math.tan(x);
            double actual = func.compute(x);
            assertEquals(expected, actual, 1e-7);
        }
    }
}