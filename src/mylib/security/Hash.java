package mylib.security;

import java.security.MessageDigest;

public final class Hash {

    private Hash(){
    }

    public static byte[] sha1(byte[] input){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            return digest.digest(input);
        }catch(Exception ignored){}
        return null;
    }

    public static byte[] sha256(byte[] input){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(input);
        }catch(Exception ignored){}
        return null;
    }

    public static byte[] sha512(byte[] input){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            return digest.digest(input);
        }catch(Exception ignored){}
        return null;
    }

    public static byte[] md5(byte[] input){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(input);
        }catch (Exception ignored){}
        return null;
    }

}
