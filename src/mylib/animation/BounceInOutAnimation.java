package mylib.animation;

public class BounceInOutAnimation implements Animation {
    @Override
    public double eval(double t, double b, double c, double d) {
        if (t < d / 2) return bounceIn(t * 2, 0, c, d) * .5 + b;
        return bounceOut(t * 2 - d, 0, c, d) * .5 + c * .5 + b;
    }

    protected double bounceOut(double t, double b, double c, double d){
        if ((t /= d) < (1 / 2.75)) {
            return c * (7.5625*t*t) + b;
        }
        else if (t < (2 / 2.75)) {
            return c * (7.5625*(t -= (1.5 / 2.75))*t + .75) + b;
        }
        else if (t < (2.5 / 2.75)) {
            return c * (7.5625*(t -= (2.25 / 2.75))*t + .9375) + b;
        }
        else {
            return c * (7.5625*(t -= (2.625 / 2.75))*t + .984375) + b;
        }
    }

    protected double bounceIn(double t, double b, double c, double d){
        return c - bounceOut(d - t, 0, c, d) + b;
    }
}
