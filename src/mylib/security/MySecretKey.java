package mylib.security;

import java.io.Serializable;
import java.math.BigInteger;

public class MySecretKey implements Serializable {

    protected final BigInteger d, n;

    MySecretKey(BigInteger d, BigInteger n){
        this.d = d;
        this.n = n;
    }

    @Override
    public String toString() {
        return '[' + d.toString() + ", " + n.toString() + ']';
    }

}
