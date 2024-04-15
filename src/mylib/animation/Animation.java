package mylib.animation;

public interface Animation {

    /**
     * evaluates the next state
     * @param t current time: moment where the object is
     * @param b beginning value: the position where the object is at the moment t
     * @param c change in value: the difference of value between the current
     *         position and the final position
     * @param d Duration: the total number of iterations that you want your
     *          animation to have, needs to be the current unit as the starting time.
     * @return the next state
     */
    double eval(double t, double b, double c, double d);

}
