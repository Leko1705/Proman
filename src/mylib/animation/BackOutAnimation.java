package mylib.animation;

public class BackOutAnimation implements Animation {

    @Override
    public double eval(double t, double b, double c, double d) {
        double s = 1.70158;
        return c * ((t = t / d - 1)*t*((s + 1)*t + s) + 1) + b;
    }
}
