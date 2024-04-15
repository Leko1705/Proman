package mylib;

public final class Search {

    private Search(){
    }

    public static int linear(double[] a, double d){
        for (int i = 0; i < a.length; i++)
            if (a[i] == d)
                return i;
        return -1;
    }

    public static int binary(double[] a, double d){
        int l = 0, r = a.length;
        while (l < r){
            int center = l + (r - l) / 2;
            if (a[center] == d) return center;
            if (r - l == 1) break;
            if (a[center] < d) l = center;
            else r = center;
        }
        return -1;
    }

}
