import functions.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

class CombinedFunctionIntegrationTest {
    private static final String NEGATIVE_CSV = "negative_func_mock.csv";
    private static final String POSITIVE_CSV = "positive_func_mock.csv";
    private static Map<Double, Double> negativeValuesFromCsv;
    private static Map<Double, Double> positiveValuesFromCsv;

    @BeforeAll
    static void generateCsvAndLoadValues() throws IOException {
        LnSeries realLn = new LnSeries(1e-15);
        SinSeries realSin = new SinSeries(1e-15);
        CosSeries realCos = new CosSeries(realSin);
        TanSeries realTan = new TanSeries(realSin, realCos);
        Log2Series realLog2 = new Log2Series(realLn);
        Log5Series realLog5 = new Log5Series(realLn);
        Log10Series realLog10 = new Log10Series(realLn);

        NegativeXFunction realNegative = new NegativeXFunction(realCos, realTan);
        PositiveXFunction realPositive = new PositiveXFunction(realLog2, realLog5, realLog10);

        double negStart = -6;
        double negEnd = 0;
        double negStep = 0.001;
        List<double[]> negData = new ArrayList<>();
        for (double x = negStart; x <= negEnd; x += negStep) {
            if (Math.abs(Math.cos(x)) < 1e-10) continue;
            negData.add(new double[]{x, realNegative.compute(x)});
        }
        CsvUtils.write(NEGATIVE_CSV, negData.toArray(new double[0][]));

        double posStart = 0.5;
        double posEnd = 10;
        double posStep = 0.5;
        List<double[]> posData = new ArrayList<>();
        for (double x = posStart; x <= posEnd; x += posStep) {
            posData.add(new double[]{x, realPositive.compute(x)});
        }
        CsvUtils.write(POSITIVE_CSV, posData.toArray(new double[0][]));

        negativeValuesFromCsv = CsvUtils.read(NEGATIVE_CSV);
        positiveValuesFromCsv = CsvUtils.read(POSITIVE_CSV);
    }

    @AfterAll
    static void cleanup() throws IOException {
        Files.deleteIfExists(Path.of(NEGATIVE_CSV));
        Files.deleteIfExists(Path.of(POSITIVE_CSV));
    }

    @Test
    void testCombinedFunctionUsingMocksFromSeries() {
        NegativeXFunction mockNegative = mock(NegativeXFunction.class);
        PositiveXFunction mockPositive = mock(PositiveXFunction.class);

        when(mockNegative.compute(anyDouble())).thenAnswer(invocation -> {
            double x = invocation.getArgument(0);
            for (Map.Entry<Double, Double> entry : negativeValuesFromCsv.entrySet()) {
                if (Math.abs(entry.getKey() - x) <= 1e-4) {
                    return entry.getValue();
                }
            }
            throw new IllegalArgumentException("Negative: значение для x=" + x + " не найдено");
        });

        when(mockPositive.compute(anyDouble())).thenAnswer(invocation -> {
            double x = invocation.getArgument(0);
            for (Map.Entry<Double, Double> entry : positiveValuesFromCsv.entrySet()) {
                if (Math.abs(entry.getKey() - x) <= 1e-3) {
                    return entry.getValue();
                }
            }
            throw new IllegalArgumentException("Positive: значение для x=" + x + " не найдено");
        });

        CombinedFunction combined = new CombinedFunction(mockNegative, mockPositive);

        double start = -5;
        double end = 5;
        double step = 0.5;
        for (double x = start; x <= end; x += step) {
            if (Math.abs(Math.cos(x)) < 1e-10 || x == 0) continue;
            double expected;
            if (x <= 0) {
                expected = Math.cos(x) - Math.tan(x);
            } else {
                expected = computePositiveExpected(x);
            }
            double actual = combined.compute(x);
            assertEquals(expected, actual, 1e-3);
        }
    }

    @Test
    void testCombinedFunctionUsingRealSeries() {
        LnSeries realLn = new LnSeries(1e-15);
        SinSeries realSin = new SinSeries(1e-15);
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
        for (double x = start; x <= end; x += step) {
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