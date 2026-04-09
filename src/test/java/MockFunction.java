import java.io.IOException;
import java.util.*;

public class MockFunction {
    private final List<Double> xValues;
    private final Map<Double, Double> table;

    public MockFunction(String csvFilename) throws IOException {
        this.table = CsvUtils.read(csvFilename);
        this.xValues = new ArrayList<>(table.keySet());
        Collections.sort(xValues);
    }

    public double apply(double x) {
        if (table.containsKey(x)) return table.get(x);
        int idx = Collections.binarySearch(xValues, x);
        if (idx >= 0) return table.get(xValues.get(idx));
        idx = -idx - 1;
        if (idx == 0) return table.get(xValues.get(0));
        if (idx == xValues.size()) return table.get(xValues.get(xValues.size() - 1));
        double x1 = xValues.get(idx - 1);
        double x2 = xValues.get(idx);
        double y1 = table.get(x1);
        double y2 = table.get(x2);
        return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
    }
}