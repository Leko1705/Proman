package mylib.security;

import java.io.Serializable;
import java.util.Arrays;

public class Certificate implements Serializable {

    private final byte[] content;
    private final byte[] cipher;

    public Certificate(MySecretKey sk, byte[] content){
        this.content = Hash.sha256(content);
        cipher = MyRSA.decrypt(this.content, sk);
    }

    public boolean validate(MyPublicKey pk){
        byte[] plainHash = MyRSA.encrypt(cipher, pk);
        return Arrays.equals(plainHash, Hash.sha256(content));
    }

    public byte[] getContent() {
        return content;
    }
}
