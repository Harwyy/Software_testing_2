import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CsvUtils {

    public static void write(String filename, double[][] data) throws IOException {
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Path.of(filename)))) {
            pw.println("x,value");
            for (double[] pair : data) {
                pw.printf(Locale.US, "%.15f,%.15f%n", pair[0], pair[1]);
            }
        }
    }

    public static Map<Double, Double> read(String filename) throws IOException {
        Map<Double, Double> map = new HashMap<>();
        try (BufferedReader br = Files.newBufferedReader(Path.of(filename))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(",");
                double x = Double.parseDouble(parts[0]);
                double y = Double.parseDouble(parts[1]);
                map.put(x, y);
            }
        }
        return map;
    }
}
