package mylib.animation;

public class ElasticInOutAnimation implements Animation {

    @Override
    public double eval(double t, double b, double c, double d) {
        if (t == 0)
            return b;
        if ((t /= d / 2) == 2)
            return b + c;
        double p = d * (.3f * 1.5f);
        double s = p / 4;
        if (t < 1)
            return -.5f * (c * Math.pow(2, 10 * (t -= 1)) * Math.sin((t * d - s) * (2 * Math.PI) / p)) + b;
        return c * Math.pow(2, -10 * (t -= 1)) * Math.sin((t * d - s) * (2 * Math.PI) / p) * .5f + c + b;
    }
}
