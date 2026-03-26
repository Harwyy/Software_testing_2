import functions.MathFunction;

import java.io.*;

public class CsvExporter {
    public static void export(String filename, double xStart, double xEnd, double step,
                              MathFunction func) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("x;f(x)");
            for (double x = xStart; x <= xEnd + step / 2; x += step) {
                double val = func.apply(x);
                writer.printf("%.6f;%.10f%n", x, val);
            }
        }
    }
}