import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.junit.Test;

public class EncryptTest {


    public static void main(String[] args) {

    }

    public void encrypt() throws Exception {

        String str = "ENCRYPT!@1234";
        String encStr = encAES(str);
        System.out.println(encStr);
        System.out.println(decAES(encStr));

    }

    public Key getAESKey() throws Exception {
        String iv;
        Key keySpec;

        String key = "encryption!@1234";
        iv = key.substring(0, 16);
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes("UTF-8");

        int len = b.length;
        if (len > keyBytes.length) {
            len = keyBytes.length;
        }

        System.arraycopy(b, 0, keyBytes, 0, len);
        keySpec = new SecretKeySpec(keyBytes, "AES");

        return keySpec;
    }

    // 암호화
    public String encAES(String str) throws Exception {
        Key keySpec = getAESKey();
        String iv = "0987654321654321";
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes("UTF-8")));
        byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
        String enStr = new String(Base64.getEncoder().encode(encrypted));

        return enStr;
    }

    // 복호화
    public String decAES(String enStr) throws Exception {
        Key keySpec = getAESKey();
        String iv = "0987654321654321";
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes("UTF-8")));
        byte[] byteStr = Base64.getDecoder().decode(enStr.getBytes("UTF-8"));
        String decStr = new String(c.doFinal(byteStr), "UTF-8");

        return decStr;
    }
}
