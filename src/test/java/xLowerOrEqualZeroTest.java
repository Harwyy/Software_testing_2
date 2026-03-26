import functions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class xLowerOrEqualZeroTest {

    private static final double EPS = 1e-10;
    private static final double DELTA = 1e-8;

    @ParameterizedTest
    @CsvSource({
            "-0.7853981633974483, 1.70710678",
            "-0.5235987755982988, 1.44337567",
            "-1.0471975511965976, 2.2320507999",
            "0.0, 1.0"
    })
    public void compareWithEtalonValue(double x, double expected){
        FuncFactory factory = new FuncFactory(EPS, false);
        MathFunction f = factory.createPiecewise();
        assertEquals(expected, f.apply(x), DELTA);
    }

    @ParameterizedTest
    @CsvSource({
            "-0.7853981633974483, 1.70710678",
            "-0.5235987755982988, 1.44337567",
            "-1.0471975511965976, 2.2320507999",
            "0.0, 1.0"
    })
    void sinRealOtherEtalon(double x, double expected) {
        FuncFactory factory = new FuncFactory(EPS, true) {
            @Override
            public MathFunction createSin() {
                return new SinFunc(EPS);
            }
        };
        MathFunction f = factory.createPiecewise();
        assertEquals(expected, f.apply(x), DELTA);
    }

    @ParameterizedTest
    @CsvSource({
            "-0.7853981633974483, 1.70710678",
            "-0.5235987755982988, 1.44337567",
            "-1.0471975511965976, 2.2320507999",
            "0.0, 1.0"
    })
    void cosRealOtherEtalon(double x, double expected) {
        FuncFactory factory = new FuncFactory(EPS, true) {
            @Override
            public MathFunction createCos() {
                return new CosFunc(createSin());
            }
        };
        MathFunction f = factory.createPiecewise();
        assertEquals(expected, f.apply(x), DELTA);
    }

    @ParameterizedTest
    @CsvSource({
            "-0.7853981633974483, 1.70710678",
            "-0.5235987755982988, 1.44337567",
            "-1.0471975511965976, 2.2320507999",
            "0.0, 1.0"
    })
    void tanRealOtherEtalon(double x, double expected) {
        FuncFactory factory = new FuncFactory(EPS, true) {
            @Override
            public MathFunction createTan() {
                return new TanFunc(createSin(), createCos());
            }
        };
        MathFunction f = factory.createPiecewise();
        assertEquals(expected, f.apply(x), DELTA);
    }

    @ParameterizedTest
    @CsvSource({
            "-0.7853981633974483, 1.70710678",
            "-0.5235987755982988, 1.44337567",
            "-1.0471975511965976, 2.2320507999",
            "0.0, 1.0"
    })
    void sinTanRealOtherEtalon(double x, double expected) {
        FuncFactory factory = new FuncFactory(EPS, true) {
            @Override
            public MathFunction createSin() {
                return new SinFunc(EPS);
            }
            @Override
            public MathFunction createTan() {
                return new TanFunc(createSin(), createCos());
            }
        };
        MathFunction f = factory.createPiecewise();
        assertEquals(expected, f.apply(x), DELTA);
    }

    @ParameterizedTest
    @CsvSource({
            "-0.7853981633974483, 1.70710678",
            "-0.5235987755982988, 1.44337567",
            "-1.0471975511965976, 2.2320507999",
            "0.0, 1.0"
    })
    void cosTanRealOtherEtalon(double x, double expected) {
        FuncFactory factory = new FuncFactory(EPS, true) {
            @Override
            public MathFunction createCos() {
                return new CosFunc(createSin());
            }
            @Override
            public MathFunction createTan() {
                return new TanFunc(createSin(), createCos());
            }
        };
        MathFunction f = factory.createPiecewise();
        assertEquals(expected, f.apply(x), DELTA);
    }

    @ParameterizedTest
    @CsvSource({
            "-0.7853981633974483, 1.70710678",
            "-0.5235987755982988, 1.44337567",
            "-1.0471975511965976, 2.2320507999",
            "0.0, 1.0"
    })
    void cosSinRealOtherEtalon(double x, double expected) {
        FuncFactory factory = new FuncFactory(EPS, true) {
            @Override
            public MathFunction createSin() {
                return new SinFunc(EPS);
            }
            @Override
            public MathFunction createCos() {
                return new CosFunc(createSin());
            }
        };
        MathFunction f = factory.createPiecewise();
        assertEquals(expected, f.apply(x), DELTA);
    }

    @ParameterizedTest
    @CsvSource({
            "-0.7853981633974483, 1.70710678",
            "-0.5235987755982988, 1.44337567",
            "-1.0471975511965976, 2.2320507999",
            "0.0, 1.0"
    })
    void cosSinTanRealOtherEtalon(double x, double expected) {
        FuncFactory factory = new FuncFactory(EPS, true) {
            @Override
            public MathFunction createSin() {
                return new SinFunc(EPS);
            }
            @Override
            public MathFunction createCos() {
                return new CosFunc(createSin());
            }
            @Override
            public MathFunction createTan() {
                return new TanFunc(createSin(), createCos());
            }
        };
        MathFunction f = factory.createPiecewise();
        assertEquals(expected, f.apply(x), DELTA);
    }
}
