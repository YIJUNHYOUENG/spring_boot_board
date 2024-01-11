package com.example.demo12131.encryption;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Encryption {
    private static String privateKey_256 = WebConstants.PRIVATE_KEY;

    public static String aesCBCEncode(String plainText) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(privateKey_256.getBytes(), "AES");
        IvParameterSpec IV = new IvParameterSpec(privateKey_256.substring(0, 16).getBytes());

        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");

        c.init(Cipher.ENCRYPT_MODE, secretKey, IV);

        byte[] encryptionByte =  c.doFinal(plainText.getBytes());
        byte[] encrypted = Base64.getEncoder().encode(encryptionByte);

        return new String(encrypted).trim();
    }

    public static String aesCBCDecode(String encodeText) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(privateKey_256.getBytes(), "AES");
        IvParameterSpec IV = new IvParameterSpec(privateKey_256.substring(0, 16).getBytes());

        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");

        c.init(Cipher.DECRYPT_MODE, secretKey, IV);

        byte[] decryptionByte =  Base64.getDecoder().decode(encodeText.getBytes());
        byte[] decrypted = c.doFinal(decryptionByte);

        return new String(decrypted).trim();
    }
}
