import functions.FuncFactory;
import functions.LnFunc;
import functions.LogFunc;
import functions.MathFunction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class xGreaterZeroTest {

    private static final double EPS = 1e-10;
    private static final double DELTA = 1e-8;

    @ParameterizedTest
    @CsvSource({
            "0.5, -0.000092759",
            "2.0, 0.00001568",
            "5.0, 0.00651381",
            "10.0, 0.03590043856361"
    })
    void compareWithEtalonValue(double x, double expected) {
        FuncFactory factory = new FuncFactory(EPS, false);
        MathFunction f = factory.createPiecewise();
        assertEquals(expected, f.apply(x), DELTA);
    }

    @ParameterizedTest
    @CsvSource({
            "0.5, -0.000092759",
            "2.0, 0.00001568",
            "5.0, 0.00651381",
            "10.0, 0.03590043856361"
    })
    void lnRealOtherEtalon(double x, double expected) {
        FuncFactory factory = new FuncFactory(EPS, true) {
            @Override
            public MathFunction createLn() {
                return new LnFunc(EPS);
            }
        };
        MathFunction f = factory.createPiecewise();
        assertEquals(expected, f.apply(x), DELTA);
    }

    @ParameterizedTest
    @CsvSource({
            "0.5, -0.000092759",
            "2.0, 0.00001568",
            "5.0, 0.00651381",
            "10.0, 0.03590043856361"
    })
    void log2RealOtherEtalon(double x, double expected) {
        FuncFactory factory = new FuncFactory(EPS, true) {
            @Override
            public MathFunction createLog2() {
                return new LogFunc(2, createLn());
            }
        };
        MathFunction f = factory.createPiecewise();
        assertEquals(expected, f.apply(x), DELTA);
    }

    @ParameterizedTest
    @CsvSource({
            "0.5, -0.000092759",
            "2.0, 0.00001568",
            "5.0, 0.00651381",
            "10.0, 0.03590043856361"
    })
    void log5RealOtherEtalon(double x, double expected) {
        FuncFactory factory = new FuncFactory(EPS, true) {
            @Override
            public MathFunction createLog5() {
                return new LogFunc(5, createLn());
            }
        };
        MathFunction f = factory.createPiecewise();
        assertEquals(expected, f.apply(x), DELTA);
    }

    @ParameterizedTest
    @CsvSource({
            "0.5, -0.000092759",
            "2.0, 0.00001568",
            "5.0, 0.00651381",
            "10.0, 0.03590043856361"
    })
    void log10RealOtherEtalon(double x, double expected) {
        FuncFactory factory = new FuncFactory(EPS, true) {
            @Override
            public MathFunction createLog10() {
                return new LogFunc(10, createLn());
            }
        };
        MathFunction f = factory.createPiecewise();
        assertEquals(expected, f.apply(x), DELTA);
    }

    @ParameterizedTest
    @CsvSource({
            "0.5, -0.000092759",
            "2.0, 0.00001568",
            "5.0, 0.00651381",
            "10.0, 0.03590043856361"
    })
    void lnLog2RealOtherEtalon(double x, double expected) {
        FuncFactory factory = new FuncFactory(EPS, true) {
            @Override
            public MathFunction createLn() {
                return new LnFunc(EPS);
            }
            @Override
            public MathFunction createLog2() {
                return new LogFunc(2, createLn());
            }
        };
        MathFunction f = factory.createPiecewise();
        assertEquals(expected, f.apply(x), DELTA);
    }

    @ParameterizedTest
    @CsvSource({
            "0.5, -0.000092759",
            "2.0, 0.00001568",
            "5.0, 0.00651381",
            "10.0, 0.03590043856361"
    })
    void lnLog5RealOtherEtalon(double x, double expected) {
        FuncFactory factory = new FuncFactory(EPS, true) {
            @Override
            public MathFunction createLn() {
                return new LnFunc(EPS);
            }
            @Override
            public MathFunction createLog5() {
                return new LogFunc(5, createLn());
            }
        };
        MathFunction f = factory.createPiecewise();
        assertEquals(expected, f.apply(x), DELTA);
    }

    @ParameterizedTest
    @CsvSource({
            "0.5, -0.000092759",
            "2.0, 0.00001568",
            "5.0, 0.00651381",
            "10.0, 0.03590043856361"
    })
    void lnLog10RealOtherEtalon(double x, double expected) {
        FuncFactory factory = new FuncFactory(EPS, true) {
            @Override
            public MathFunction createLn() {
                return new LnFunc(EPS);
            }
            @Override
            public MathFunction createLog10() {
                return new LogFunc(10, createLn());
            }
        };
        MathFunction f = factory.createPiecewise();
        assertEquals(expected, f.apply(x), DELTA);
    }

    @ParameterizedTest
    @CsvSource({
            "0.5, -0.000092759",
            "2.0, 0.00001568",
            "5.0, 0.00651381",
            "10.0, 0.03590043856361"
    })
    void log2Log5RealOtherEtalon(double x, double expected) {
        FuncFactory factory = new FuncFactory(EPS, true) {
            @Override
            public MathFunction createLog2() {
                return new LogFunc(2, createLn());
            }
            @Override
            public MathFunction createLog5() {
                return new LogFunc(5, createLn());
            }
        };
        MathFunction f = factory.createPiecewise();
        assertEquals(expected, f.apply(x), DELTA);
    }

    @ParameterizedTest
    @CsvSource({
            "0.5, -0.000092759",
            "2.0, 0.00001568",
            "5.0, 0.00651381",
            "10.0, 0.03590043856361"
    })
    void log2Log10RealOtherEtalon(double x, double expected) {
        FuncFactory factory = new FuncFactory(EPS, true) {
            @Override
            public MathFunction createLog2() {
                return new LogFunc(2, createLn());
            }
            @Override
            public MathFunction createLog10() {
                return new LogFunc(10, createLn());
            }
        };
        MathFunction f = factory.createPiecewise();
        assertEquals(expected, f.apply(x), DELTA);
    }

    @ParameterizedTest
    @CsvSource({
            "0.5, -0.000092759",
            "2.0, 0.00001568",
            "5.0, 0.00651381",
            "10.0, 0.03590043856361"
    })
    void log5Log10RealOtherEtalon(double x, double expected) {
        FuncFactory factory = new FuncFactory(EPS, true) {
            @Override
            public MathFunction createLog5() {
                return new LogFunc(5, createLn());
            }
            @Override
            public MathFunction createLog10() {
                return new LogFunc(10, createLn());
            }
        };
        MathFunction f = factory.createPiecewise();
        assertEquals(expected, f.apply(x), DELTA);
    }

    @ParameterizedTest
    @CsvSource({
            "0.5, -0.000092759",
            "2.0, 0.00001568",
            "5.0, 0.00651381",
            "10.0, 0.03590043856361"
    })
    void lnLog2Log5RealOtherEtalon(double x, double expected) {
        FuncFactory factory = new FuncFactory(EPS, true) {
            @Override
            public MathFunction createLn() {
                return new LnFunc(EPS);
            }
            @Override
            public MathFunction createLog2() {
                return new LogFunc(2, createLn());
            }
            @Override
            public MathFunction createLog5() {
                return new LogFunc(5, createLn());
            }
        };
        MathFunction f = factory.createPiecewise();
        assertEquals(expected, f.apply(x), DELTA);
    }

    @ParameterizedTest
    @CsvSource({
            "0.5, -0.000092759",
            "2.0, 0.00001568",
            "5.0, 0.00651381",
            "10.0, 0.03590043856361"
    })
    void lnLog2Log10RealOtherEtalon(double x, double expected) {
        FuncFactory factory = new FuncFactory(EPS, true) {
            @Override
            public MathFunction createLn() {
                return new LnFunc(EPS);
            }
            @Override
            public MathFunction createLog2() {
                return new LogFunc(2, createLn());
            }
            @Override
            public MathFunction createLog10() {
                return new LogFunc(10, createLn());
            }
        };
        MathFunction f = factory.createPiecewise();
        assertEquals(expected, f.apply(x), DELTA);
    }

    @ParameterizedTest
    @CsvSource({
            "0.5, -0.000092759",
            "2.0, 0.00001568",
            "5.0, 0.00651381",
            "10.0, 0.03590043856361"
    })
    void lnLog5Log10RealOtherEtalon(double x, double expected) {
        FuncFactory factory = new FuncFactory(EPS, true) {
            @Override
            public MathFunction createLn() {
                return new LnFunc(EPS);
            }
            @Override
            public MathFunction createLog5() {
                return new LogFunc(5, createLn());
            }
            @Override
            public MathFunction createLog10() {
                return new LogFunc(10, createLn());
            }
        };
        MathFunction f = factory.createPiecewise();
        assertEquals(expected, f.apply(x), DELTA);
    }

    @ParameterizedTest
    @CsvSource({
            "0.5, -0.000092759",
            "2.0, 0.00001568",
            "5.0, 0.00651381",
            "10.0, 0.03590043856361"
    })
    void log2Log5Log10RealOtherEtalon(double x, double expected) {
        FuncFactory factory = new FuncFactory(EPS, true) {
            @Override
            public MathFunction createLog2() {
                return new LogFunc(2, createLn());
            }
            @Override
            public MathFunction createLog5() {
                return new LogFunc(5, createLn());
            }
            @Override
            public MathFunction createLog10() {
                return new LogFunc(10, createLn());
            }
        };
        MathFunction f = factory.createPiecewise();
        assertEquals(expected, f.apply(x), DELTA);
    }

    @Test
    void xEqualsOneThrowsException() {
        FuncFactory factory = new FuncFactory(EPS, false);
        MathFunction f = factory.createPiecewise();
        assertThrows(ArithmeticException.class, () -> f.apply(1.0));
    }


}
