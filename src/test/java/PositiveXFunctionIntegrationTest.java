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

class PositiveXFunctionIntegrationTest {
    private static final String LOG2_SERIES_CSV = "log2_series_mock.csv";
    private static final String LOG5_SERIES_CSV = "log5_series_mock.csv";
    private static final String LOG10_SERIES_CSV = "log10_series_mock.csv";
    private static Map<Double, Double> log2ValuesFromCsv;
    private static Map<Double, Double> log5ValuesFromCsv;
    private static Map<Double, Double> log10ValuesFromCsv;

    @BeforeAll
    static void generateCsvAndLoadValues() throws IOException {
        LnSeries realLn = new LnSeries(1e-15);
        Log2Series realLog2 = new Log2Series(realLn);
        Log5Series realLog5 = new Log5Series(realLn);
        Log10Series realLog10 = new Log10Series(realLn);

        double start = 1;
        double end = 1000;
        double step = 1;

        List<double[]> log2Data = new ArrayList<>();
        List<double[]> log5Data = new ArrayList<>();
        List<double[]> log10Data = new ArrayList<>();

        for (double x = start; x <= end; x += step) {
            log2Data.add(new double[]{x, realLog2.log2(x)});
            log5Data.add(new double[]{x, realLog5.log5(x)});
            log10Data.add(new double[]{x, realLog10.log10(x)});
        }

        CsvUtils.write(LOG2_SERIES_CSV, log2Data.toArray(new double[0][]));
        CsvUtils.write(LOG5_SERIES_CSV, log5Data.toArray(new double[0][]));
        CsvUtils.write(LOG10_SERIES_CSV, log10Data.toArray(new double[0][]));

        log2ValuesFromCsv = CsvUtils.read(LOG2_SERIES_CSV);
        log5ValuesFromCsv = CsvUtils.read(LOG5_SERIES_CSV);
        log10ValuesFromCsv = CsvUtils.read(LOG10_SERIES_CSV);
    }

    @AfterAll
    static void cleanup() throws IOException {
        Files.deleteIfExists(Path.of(LOG2_SERIES_CSV));
        Files.deleteIfExists(Path.of(LOG5_SERIES_CSV));
        Files.deleteIfExists(Path.of(LOG10_SERIES_CSV));
    }

    @Test
    void testPositiveXFunctionUsingMocksFromSeries() {
        Log2Series mockLog2 = mock(Log2Series.class);
        Log5Series mockLog5 = mock(Log5Series.class);
        Log10Series mockLog10 = mock(Log10Series.class);

        when(mockLog2.log2(anyDouble())).thenAnswer(invocation -> {
            double x = invocation.getArgument(0);
            for (Map.Entry<Double, Double> entry : log2ValuesFromCsv.entrySet()) {
                if (Math.abs(entry.getKey() - x) <= 1e-3) {
                    return entry.getValue();
                }
            }
            throw new IllegalArgumentException("log2: значение для x=" + x + " не найдено");
        });

        when(mockLog5.log5(anyDouble())).thenAnswer(invocation -> {
            double x = invocation.getArgument(0);
            for (Map.Entry<Double, Double> entry : log5ValuesFromCsv.entrySet()) {
                if (Math.abs(entry.getKey() - x) <= 1e-3) {
                    return entry.getValue();
                }
            }
            throw new IllegalArgumentException("log5: значение для x=" + x + " не найдено");
        });

        when(mockLog10.log10(anyDouble())).thenAnswer(invocation -> {
            double x = invocation.getArgument(0);
            for (Map.Entry<Double, Double> entry : log10ValuesFromCsv.entrySet()) {
                if (Math.abs(entry.getKey() - x) <= 1e-3) {
                    return entry.getValue();
                }
            }
            throw new IllegalArgumentException("log10: значение для x=" + x + " не найдено");
        });

        PositiveXFunction func = new PositiveXFunction(mockLog2, mockLog5, mockLog10);

        double start = 1;
        double end = 10;
        double step = 1;
        for (double x = start; x <= end; x += step) {
            double expected = computeExpected(x);
            double actual = func.compute(x);
            assertEquals(expected, actual, 1e-3);
        }
    }

    @Test
    void testPositiveXFunctionUsingRealSeries() {
        LnSeries realLn = new LnSeries(1e-15);
        Log2Series realLog2 = new Log2Series(realLn);
        Log5Series realLog5 = new Log5Series(realLn);
        Log10Series realLog10 = new Log10Series(realLn);
        PositiveXFunction func = new PositiveXFunction(realLog2, realLog5, realLog10);

        double start = 1;
        double end = 10;
        double step = 1;
        for (double x = start; x <= end; x += step) {
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