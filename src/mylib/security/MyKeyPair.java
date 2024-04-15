package mylib.security;


public class MyKeyPair {

    private final MyPublicKey pk;
    private final MySecretKey sk;

    protected MyKeyPair(MyPublicKey pk, MySecretKey sk){
        this.pk = pk;
        this.sk = sk;
    }

    public MyPublicKey getPublicKey() {
        return pk;
    }

    public MySecretKey getSecretKey() {
        return sk;
    }

    @Override
    public String toString() {
        return '[' + pk.toString() + ", " + sk.toString() + ']';
    }
}
