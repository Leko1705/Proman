package mylib.animation;

public class ExpInOutAnimation implements Animation {

    @Override
    public double eval(double t, double b, double c, double d) {
        t /= d / 2;
        if (t < 1) return c / 2 * Math.pow(2, 10 * (t - 1)) + b;
        t--;
        return c / 2 * (-Math.pow(2, -10 * t) + 2) + b;
    }
}
