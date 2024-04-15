package mylib.animation;

public class BounceInAnimation implements Animation {

    @Override
    public double eval(double t, double b, double c, double d) {
        return c - bounceOut(d - t, c, d) + b;
    }

    private double bounceOut(double t, double c, double d){
        if ((t /= d) < (1 / 2.75)) {
            return c * (7.5625*t*t);
        }
        else if (t < (2 / 2.75)) {
            return c * (7.5625*(t -= (1.5 / 2.75))*t + .75);
        }
        else if (t < (2.5 / 2.75)) {
            return c * (7.5625*(t -= (2.25 / 2.75))*t + .9375);
        }
        else {
            return c * (7.5625*(t -= (2.625 / 2.75))*t + .984375);
        }
    }
}
