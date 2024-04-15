package mylib.animation;

public class ExpOutAnimation implements Animation {

    @Override
    public double eval(double t, double b, double c, double d) {
        return c * (-Math.pow(2, -10 * t / d) + 1) + b;
    }
}
