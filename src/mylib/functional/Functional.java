package mylib.functional;

public interface Functional extends Evaluable<Double, Double> {

    @Override
    Double eval(Double... input);

    default double gradient(Double... input){
        return Functions.gradient(this, input);
    }
}
