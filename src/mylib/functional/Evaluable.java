package mylib.functional;

public interface Evaluable<I, R> {

    R eval(I... input);

}
