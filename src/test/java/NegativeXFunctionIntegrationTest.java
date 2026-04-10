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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

class NegativeXFunctionIntegrationTest {
    private static final String COS_SERIES_CSV = "cos_series_mock.csv";
    private static final String TAN_SERIES_CSV = "tan_series_mock.csv";
    private static Map<Double, Double> cosValuesFromCsv;
    private static Map<Double, Double> tanValuesFromCsv;

    @BeforeAll
    static void generateCsvAndLoadValues() throws IOException {
        SinSeries realSin = new SinSeries(1e-15);
        CosSeries realCos = new CosSeries(realSin);
        TanSeries realTan = new TanSeries(realSin, realCos);

        double start = -6;
        double end = 6;
        double step = 0.001;

        List<double[]> cosData = new ArrayList<>();
        List<double[]> tanData = new ArrayList<>();

        for (double x = start; x <= end; x += step) {
            cosData.add(new double[]{x, realCos.cos(x)});
            if (Math.abs(realCos.cos(x)) > 1e-12) {
                tanData.add(new double[]{x, realTan.tan(x)});
            }
        }

        CsvUtils.write(COS_SERIES_CSV, cosData.toArray(new double[0][]));
        CsvUtils.write(TAN_SERIES_CSV, tanData.toArray(new double[0][]));

        cosValuesFromCsv = CsvUtils.read(COS_SERIES_CSV);
        tanValuesFromCsv = CsvUtils.read(TAN_SERIES_CSV);
    }

    @AfterAll
    static void cleanup() throws IOException {
        Files.deleteIfExists(Path.of(COS_SERIES_CSV));
        Files.deleteIfExists(Path.of(TAN_SERIES_CSV));
    }

    @Test
    void testNegativeXFunctionUsingMocksFromSeries() {
        CosSeries mockCos = mock(CosSeries.class);
        TanSeries mockTan = mock(TanSeries.class);

        when(mockCos.cos(anyDouble())).thenAnswer(invocation -> {
            double x = invocation.getArgument(0);
            for (Map.Entry<Double, Double> entry : cosValuesFromCsv.entrySet()) {
                if (Math.abs(entry.getKey() - x) <= 1e-3) {
                    return entry.getValue();
                }
            }
            throw new IllegalArgumentException("Cos: значение для x=" + x + " не найдено");
        });

        when(mockTan.tan(anyDouble())).thenAnswer(invocation -> {
            double x = invocation.getArgument(0);
            for (Map.Entry<Double, Double> entry : tanValuesFromCsv.entrySet()) {
                if (Math.abs(entry.getKey() - x) <= 1e-3) {
                    return entry.getValue();
                }
            }
            throw new IllegalArgumentException("Tan: значение для x=" + x + " не найдено");
        });

        NegativeXFunction func = new NegativeXFunction(mockCos, mockTan);


        double start = -3.15;
        double end = 0;
        double step = 0.25;

        for (double x = start; x <= end; x += step) {
            if (Math.abs(Math.cos(x)) < 1e-10) continue;
            double expected = Math.cos(x) - Math.tan(x);
            double actual = func.compute(x);
            assertEquals(expected, actual, 1e-2);
        }
    }

    @Test
    void testNegativeXFunctionUsingRealSeries() {
        SinSeries realSin = new SinSeries(1e-15);
        CosSeries realCos = new CosSeries(realSin);
        TanSeries realTan = new TanSeries(realSin, realCos);
        NegativeXFunction func = new NegativeXFunction(realCos, realTan);

        double start = -3.15;
        double end = 0;
        double step = 0.25;

        for (double x = start; x <= end + 1e-12; x += step) {
            if (Math.abs(Math.cos(x)) < 1e-10) continue;
            double expected = Math.cos(x) - Math.tan(x);
            double actual = func.compute(x);
            assertEquals(expected, actual, 1e-7);
        }
    }
}