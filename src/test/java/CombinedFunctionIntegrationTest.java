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

class CombinedFunctionIntegrationTest {
    private static final String negativeCsv = "negative_func_mock.csv";
    private static final String positiveCsv = "positive_func_mock.csv";

    @BeforeAll
    static void generateCsvFiles() throws IOException {
        LnSeries realLn = new LnSeries(1e-12);
        SinSeries realSin = new SinSeries(1e-12);
        CosSeries realCos = new CosSeries(realSin);
        TanSeries realTan = new TanSeries(realSin, realCos);
        Log2Series realLog2 = new Log2Series(realLn);
        Log5Series realLog5 = new Log5Series(realLn);
        Log10Series realLog10 = new Log10Series(realLn);

        NegativeXFunction realNegative = new NegativeXFunction(realCos, realTan);
        PositiveXFunction realPositive = new PositiveXFunction(realLog2, realLog5, realLog10);

        double negStart = -5;
        double negEnd = 0;
        double step = 0.01;
        int negSteps = (int) Math.round((negEnd - negStart) / step);
        List<double[]> negData = new ArrayList<>();
        for (int i = 0; i <= negSteps; i++) {
            double x = negStart + i * step;
            if (Math.abs(Math.cos(x)) < 1e-10) continue;
            double value = realNegative.compute(x);
            negData.add(new double[]{x, value});
        }
        CsvUtils.write(negativeCsv, negData.toArray(new double[0][]));

        double posStart = 0.1;
        double posEnd = 5;
        int posSteps = (int) Math.round((posEnd - posStart) / step);
        List<double[]> posData = new ArrayList<>();
        for (int i = 0; i <= posSteps; i++) {
            double x = posStart + i * step;
            double value = realPositive.compute(x);
            posData.add(new double[]{x, value});
        }
        CsvUtils.write(positiveCsv, posData.toArray(new double[0][]));
    }

    @AfterAll
    static void cleanup() throws IOException {
        Files.deleteIfExists(Path.of(negativeCsv));
        Files.deleteIfExists(Path.of(positiveCsv));
    }

    @Test
    void testCombinedFunctionWithMocks() throws IOException {
        MockFunction mockNegative = new MockFunction(negativeCsv);
        NegativeXFunction negativeMock = new NegativeXFunction(null, null) {
            @Override
            public double compute(double x) {
                if (x > 0) throw new IllegalArgumentException("x <= 0");
                return mockNegative.apply(x);
            }
        };

        MockFunction mockPositive = new MockFunction(positiveCsv);
        PositiveXFunction positiveMock = new PositiveXFunction(null, null, null) {
            @Override
            public double compute(double x) {
                if (x <= 0) throw new IllegalArgumentException("x > 0");
                return mockPositive.apply(x);
            }
        };

        CombinedFunction combined = new CombinedFunction(negativeMock, positiveMock);

        double start = -5;
        double end = 5;
        double step = 0.5;
        for (double x = start; x <= end + 1e-12; x += step) {
            if (Math.abs(Math.cos(x)) < 1e-10) continue;
            double expected;
            if (x <= 0) {
                expected = Math.cos(x) - Math.tan(x);
            } else {
                expected = computePositiveExpected(x);
            }
            double actual = combined.compute(x);
            assertEquals(expected, actual, 1e-7);
        }
    }

    @Test
    void testCombinedFunctionWithRealSeries() {
        LnSeries realLn = new LnSeries(1e-12);
        SinSeries realSin = new SinSeries(1e-12);
        CosSeries realCos = new CosSeries(realSin);
        TanSeries realTan = new TanSeries(realSin, realCos);
        Log2Series realLog2 = new Log2Series(realLn);
        Log5Series realLog5 = new Log5Series(realLn);
        Log10Series realLog10 = new Log10Series(realLn);

        NegativeXFunction realNegative = new NegativeXFunction(realCos, realTan);
        PositiveXFunction realPositive = new PositiveXFunction(realLog2, realLog5, realLog10);

        CombinedFunction combined = new CombinedFunction(realNegative, realPositive);

        double start = -5;
        double end = 5;
        double step = 0.5;
        for (double x = start; x <= end + 1e-12; x += step) {
            if (Math.abs(Math.cos(x)) < 1e-10) continue;
            double expected;
            if (x <= 0) {
                expected = Math.cos(x) - Math.tan(x);
            } else {
                expected = computePositiveExpected(x);
            }
            double actual = combined.compute(x);
            assertEquals(expected, actual, 1e-7);
        }
    }

    private double computePositiveExpected(double x) {
        double log5Val = Math.log(x * x) / Math.log(5);
        double log10Val = Math.log(x * x * x) / Math.log(10);
        double sum = log5Val + log10Val;
        double square = sum * sum;
        double cube = square * square * square;
        double log2Val = Math.log(x * x * x) / Math.log(2);
        return cube / log2Val;
    }
}