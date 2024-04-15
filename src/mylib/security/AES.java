package mylib.security;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import static java.nio.charset.StandardCharsets.UTF_8;

@SuppressWarnings("unused")
public final class AES {

    private static final byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private static final int DEFAULT_ITERATIONS = 65536;
    private static final byte[] DEFAULT_SALT = "a".getBytes();

    private AES(){
    }

    public static byte[] encrypt(byte[] msg, byte[] key){
        return encrypt(msg, key, DEFAULT_ITERATIONS, DEFAULT_SALT);
    }

    public static byte[] encrypt(byte[] msg, byte[] key, int iterations){
        return encrypt(msg, key, iterations, DEFAULT_SALT);
    }

    public static byte[] encrypt(byte[] msg, byte[] key, int iterations, byte[] salt){

        try {
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec =
                    new PBEKeySpec(new String(key, UTF_8).toCharArray(), salt, iterations, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            return cipher.doFinal(msg);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] decrypt(byte[] cipherText, byte[] key) throws InvalidKeyException {
        return decrypt(cipherText, key, DEFAULT_ITERATIONS, DEFAULT_SALT);
    }

    public static byte[] decrypt(byte[] cipherText, byte[] key, int iterations) throws InvalidKeyException {
        return decrypt(cipherText, key, iterations, DEFAULT_SALT);
    }

    public static byte[] decrypt(byte[] cipherText, byte[] key, int iterations, byte[] salt) throws InvalidKeyException {

        try {
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec =
                    new PBEKeySpec(new String(key, UTF_8).toCharArray(), salt, iterations, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            return cipher.doFinal(cipherText);
        } catch (InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException |
                 NoSuchPaddingException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new InvalidKeyException(new String(key));
        }
    }

}
