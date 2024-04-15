package mylib.security;

import java.io.Serializable;
import java.math.BigInteger;

public class MyPublicKey implements Serializable {

    protected final BigInteger e, n;

    protected MyPublicKey(BigInteger e, BigInteger n){
        this.e = e;
        this.n = n;
    }

    @Override
    public String toString() {
        return '[' + e.toString() + ", " + n.toString() + ']';
    }
}
