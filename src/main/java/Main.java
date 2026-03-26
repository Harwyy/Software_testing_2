import functions.FuncFactory;
import functions.MathFunction;
import functions.SinFunc;

public class Main {
    public static void main(String[] args) {
        double eps = 0.1;
        FuncFactory factory = new FuncFactory(eps);
//        MathFunction func = factory.createPiecewise();
        MathFunction func = new SinFunc(eps);

//        double[] testPoints = {
//                -Math.PI/2,
//                -Math.PI/4,
//                0.0,
//                0.5,
//                1.0,
//                2.0,
//                5.0
//        };
//
//        for (double x : testPoints) {
//            try {
//                double val = func.apply(x);
//                System.out.printf("f(%.3f) = %.10f%n", x, val);
//            } catch (Exception e) {
//                System.out.printf("f(%.3f) -> Ошибка: %s%n", x, e.getMessage());
//            }
//        }

        try {
            CsvExporter.export("func_output.csv", -3.0, 3.0, 0.1, func);
            System.out.println("Файл 'func_output.csv' успешно создан.");
        } catch (Exception e) {
            System.err.println("Ошибка при записи CSV: " + e.getMessage());
        }
    }
}