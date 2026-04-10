import functions.CosSeries;
import functions.SinSeries;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class CosSeriesIntegrationTest {
    private static final String SIN_SERIES_CSV = "sin_series_mock.csv";
    private static Map<Double, Double> sinValuesFromCsv;
    private static SinSeries realSin;

    @BeforeAll
    static void generateCsvAndLoadValues() throws IOException {
        realSin = new SinSeries(1e-15);

        double start = -6;
        double end = 6;
        double step = 0.001;

        List<double[]> sinSeriesData = new ArrayList<>();

        for (double x = start; x <= end; x += step) {
            sinSeriesData.add(new double[]{x, realSin.sin(x)});
        }

        CsvUtils.write(SIN_SERIES_CSV, sinSeriesData.toArray(new double[0][]));
        sinValuesFromCsv = CsvUtils.read(SIN_SERIES_CSV);
    }

    @AfterAll
    static void cleanup() throws IOException {
        Files.deleteIfExists(Path.of(SIN_SERIES_CSV));
    }

    @Test
    void testCosUsingMockSinFromSeries() {
        SinSeries mockSin = mock(SinSeries.class);

        double start = -3.15;
        double end = 3.15;
        double step = 0.25;

        when(mockSin.sin(anyDouble())).thenAnswer(invocation -> {
            double x = invocation.getArgument(0);
            for (Map.Entry<Double, Double> entry : sinValuesFromCsv.entrySet()) {
                if (Math.abs(entry.getKey() - x) <= 1e-3) {
                    return entry.getValue();
                }
            }
            throw new IllegalArgumentException("Не найдено значение для x = " + x);
        });

        CosSeries cosSeries = new CosSeries(mockSin);

        for (double x = start; x <= end + 1e-12; x += step) {
            double expected = Math.cos(x);
            double actual = cosSeries.cos(x);
            assertEquals(expected, actual, 1e-3);
        }
    }

    @Test
    void testCosUsingSinSeries() {
        SinSeries realSin = new SinSeries(1e-15);
        CosSeries cosSeries = new CosSeries(realSin);

        double start = -3 * Math.PI;
        double end = 3 * Math.PI;
        double step = 0.3;
        for (double x = start; x <= end; x += step) {
            double expected = Math.cos(x);
            double actual = cosSeries.cos(x);
            assertEquals(expected, actual, 1e-7);
        }
    }
}