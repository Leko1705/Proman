package mylib.animation;

public class LinearAnimation implements Animation {

    public double eval(double t, double b, double c, double d) {
        return c * t / d + b;
    }
}
