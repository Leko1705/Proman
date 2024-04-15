package mylib.security;

import java.math.BigInteger;
import java.security.SecureRandom;

public final class MyRSA {

    /**
     * never let anyone instantiate this class
     */
    private MyRSA(){
    }

    public static MyKeyPair keyGen(int keySize){
        SecureRandom random = new SecureRandom();
        BigInteger p = new BigInteger(keySize, random).nextProbablePrime();
        BigInteger q = new BigInteger(keySize, random).nextProbablePrime();
        BigInteger N = p.parallelMultiply(q);
        BigInteger phi = phi(p).parallelMultiply(phi(q));
        BigInteger e = BigInteger.valueOf(65537);
        BigInteger d = e.modInverse(phi);

        return new MyKeyPair(
                new MyPublicKey(e, N),
                new MySecretKey(d, N)
        );
    }


    private static BigInteger phi(BigInteger p){
        return p.subtract(BigInteger.ONE);
    }

    public static byte[] encrypt(byte[] msg, MyPublicKey pk){
        BigInteger n = pk.n, e = pk.e, m = new BigInteger(msg);
        return m.modPow(e, n).toByteArray();
    }

    public static byte[] decrypt(byte[] cipher, MySecretKey sk){
        BigInteger n = sk.n, d = sk.d, c = new BigInteger(cipher);
        return c.modPow(d, n).toByteArray();
    }

}
