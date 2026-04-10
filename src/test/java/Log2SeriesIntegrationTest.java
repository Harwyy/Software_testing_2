import functions.LnSeries;
import functions.Log2Series;
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

class Log2SeriesIntegrationTest {
    private static final String LN_SERIES_CSV = "log2_series_mock.csv";
    private static Map<Double, Double> lnValuesFromCsv;

    @BeforeAll
    static void generateCsvAndLoadValues() throws IOException {
        LnSeries realLn = new LnSeries(1e-15);

        double start = 1;
        double end = 10;
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
    void testLog2UsingMockLnFromSeries() {
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

        Log2Series log2 = new Log2Series(mockLn);

        double start = 1;
        double end = 10;
        double step = 1;
        for (double x = start; x <= end; x += step) {
            double expected = Math.log(x) / Math.log(2);
            double actual = log2.log2(x);
            assertEquals(expected, actual, 1e-7);
        }
    }

    @Test
    void testLog2UsingRealLn() {
        LnSeries realLn = new LnSeries(1e-15);
        Log2Series log2 = new Log2Series(realLn);

        double start = 1;
        double end = 10;
        double step = 1;
        for (double x = start; x <= end; x += step) {
            double expected = Math.log(x) / Math.log(2);
            double actual = log2.log2(x);
            assertEquals(expected, actual, 1e-7);
        }
    }
}