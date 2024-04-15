package mylib.functional;

import java.util.Arrays;

public final class Functions {

    private Functions(){
    }

    public static double nearestZero(Functional f){
        return nearestZero(f, 0, 10_000);
    }

    public static double nearestZero(Functional f, double start){
        return nearestZero(f, start, 10_000);
    }

    public static double nearestZero(Functional f, double start, int iterations){
        if (iterations < 0)
            throw new IllegalArgumentException("iterations < 0");

        if (f instanceof Function function)
            if (function.isConstant())
                return Double.NaN;

        double x = start;
        for (int i = 0; i < iterations; i++){
            double y = f.eval(x);
            if (y == 0.0) return x;
            x -= y/gradient(f, x);
        }
        return x;
    }

    public static double gradient(Functional f, Double... x){
        final double dt = Double.MIN_EXPONENT;
        Double[] cpy = Arrays.copyOf(x, x.length);
        for (int i = 0; i < x.length; i++)
            cpy[i] += dt;
        return (f.eval(cpy) - f.eval(x)) / dt;
    }

}
