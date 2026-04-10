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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

class Log5SeriesIntegrationTest {
    private static final String LN_SERIES_CSV = "log5_series_mock.csv";
    private static Map<Double, Double> lnValuesFromCsv;

    @BeforeAll
    static void generateCsvAndLoadValues() throws IOException {
        LnSeries realLn = new LnSeries(1e-15);

        double start = 2;
        double end = 100;
        double step = 1;

        List<double[]> lnData = new ArrayList<>();

        for (double x = start; x <= end; x += step) {
            lnData.add(new double[]{x, realLn.ln(x)});
        }

        CsvUtils.write(LN_SERIES_CSV, lnData.toArray(new double[0][]));
        lnValuesFromCsv = CsvUtils.read(LN_SERIES_CSV);
    }

    @AfterAll
    static void cleanup() throws IOException {
        Files.deleteIfExists(Path.of(LN_SERIES_CSV));
    }

    @Test
    void testLog5UsingMockLnFromSeries() {
        LnSeries mockLn = mock(LnSeries.class);

        when(mockLn.ln(anyDouble())).thenAnswer(invocation -> {
            double x = invocation.getArgument(0);
            for (Map.Entry<Double, Double> entry : lnValuesFromCsv.entrySet()) {
                if (Math.abs(entry.getKey() - x) <= 1e-3) {
                    return entry.getValue();
                }
            }
            throw new IllegalArgumentException("Не найдено значение ln для x = " + x);
        });

        Log5Series log5 = new Log5Series(mockLn);

        double start = 2;
        double end = 100;
        double step = 1;
        for (double x = start; x <= end; x += step) {
            double expected = Math.log(x) / Math.log(5);
            double actual = log5.log5(x);
            assertEquals(expected, actual, 1e-7);
        }
    }

    @Test
    void testLog5UsingRealLn() {
        LnSeries realLn = new LnSeries(1e-15);
        Log5Series log5 = new Log5Series(realLn);

        double start = 2;
        double end = 100;
        double step = 1;
        for (double x = start; x <= end; x += step) {
            double expected = Math.log(x) / Math.log(5);
            double actual = log5.log5(x);
            assertEquals(expected, actual, 1e-7);
        }
    }
}