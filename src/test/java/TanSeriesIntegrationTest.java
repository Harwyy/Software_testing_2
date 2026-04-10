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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

class TanSeriesIntegrationTest {
    private static final String SIN_SERIES_CSV = "sin_series_mock.csv";
    private static final String COS_SERIES_CSV = "cos_series_mock.csv";
    private static Map<Double, Double> sinValuesFromCsv;
    private static Map<Double, Double> cosValuesFromCsv;

    @BeforeAll
    static void generateCsvAndLoadValues() throws IOException {
        SinSeries realSin = new SinSeries(1e-15);
        CosSeries realCos = new CosSeries(realSin);

        double start = -6;
        double end = 6;
        double step = 0.001;

        List<double[]> sinData = new ArrayList<>();
        List<double[]> cosData = new ArrayList<>();

        for (double x = start; x <= end; x += step) {
            sinData.add(new double[]{x, realSin.sin(x)});
            cosData.add(new double[]{x, realCos.cos(x)});
        }

        CsvUtils.write(SIN_SERIES_CSV, sinData.toArray(new double[0][]));
        CsvUtils.write(COS_SERIES_CSV, cosData.toArray(new double[0][]));

        sinValuesFromCsv = CsvUtils.read(SIN_SERIES_CSV);
        cosValuesFromCsv = CsvUtils.read(COS_SERIES_CSV);
    }

    @AfterAll
    static void cleanup() throws IOException {
        Files.deleteIfExists(Path.of(SIN_SERIES_CSV));
        Files.deleteIfExists(Path.of(COS_SERIES_CSV));
    }

    @Test
    void testTanUsingMocksFromSeries() {
        SinSeries mockSin = mock(SinSeries.class);
        CosSeries mockCos = mock(CosSeries.class);

        when(mockSin.sin(anyDouble())).thenAnswer(invocation -> {
            double x = invocation.getArgument(0);
            for (Map.Entry<Double, Double> entry : sinValuesFromCsv.entrySet()) {
                if (Math.abs(entry.getKey() - x) <= 1e-4) {
                    return entry.getValue();
                }
            }
            throw new IllegalArgumentException("Sin: значение для x=" + x + " не найдено");
        });

        when(mockCos.cos(anyDouble())).thenAnswer(invocation -> {
            double x = invocation.getArgument(0);
            for (Map.Entry<Double, Double> entry : cosValuesFromCsv.entrySet()) {
                if (Math.abs(entry.getKey() - x) <= 1e-4) {
                    return entry.getValue();
                }
            }
            throw new IllegalArgumentException("Cos: значение для x=" + x + " не найдено");
        });

        TanSeries tanSeries = new TanSeries(mockSin, mockCos);

        double start = -3.15;
        double end = 3.15;
        double step = 0.25;

        for (double x = start; x <= end; x += step) {
            if (Math.abs(Math.cos(x)) < 1e-10) continue;
            double expected = Math.tan(x);
            double actual = tanSeries.tan(x);
            assertEquals(expected, actual, 1e-3);
        }
    }

    @Test
    void testTanUsingRealSeries() {
        SinSeries realSin = new SinSeries(1e-15);
        CosSeries realCos = new CosSeries(realSin);
        TanSeries tanSeries = new TanSeries(realSin, realCos);

        double start = -3.15;
        double end = 3.15;
        double step = 0.25;

        for (double x = start; x <= end; x += step) {
            if (Math.abs(Math.cos(x)) < 1e-10) continue;
            double expected = Math.tan(x);
            double actual = tanSeries.tan(x);
            assertEquals(expected, actual, 1e-5);
        }
    }
}