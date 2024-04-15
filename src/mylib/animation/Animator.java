package mylib.animation;

public class Animator {

    private final Animation animation;
    private final double  start, distance, totalIterations;
    private double iteration = 0;

    public Animator(Animation animation, double start, double end, double fps, int evalTimeMilli){
        this.animation = animation;
        this.start = start;
        this.totalIterations = (double) evalTimeMilli / 1000 * fps;
        this.distance = end - start;
    }

    public double step(){
        double val = animation.eval(iteration, start, distance, totalIterations);

        if (iteration < totalIterations)
            iteration += 1;

        return val;
    }

    public boolean finished(){
        return iteration >= totalIterations;
    }

}
