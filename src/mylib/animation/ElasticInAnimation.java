package mylib.animation;

public class ElasticInAnimation implements Animation {

    @Override
    public double eval(double t, double b, double c, double d) {
        if (t == 0)
            return b;
        if ((t /= d) == 1)
            return b + c;
        double p = d * .3f;
        double s = p / 4;
        return -(c * Math.pow(2, 10 * (t -= 1)) * Math.sin((t * d - s) * (2 * Math.PI) / p)) + b;
    }
}
