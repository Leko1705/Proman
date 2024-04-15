package mylib.animation;

public class BackInAnimation implements Animation {

    @Override
    public double eval(double t, double b, double c, double d) {
        double s = 1.70158;
        return c * (t /= d)*t*((s + 1)*t - s) + b;
    }
}
