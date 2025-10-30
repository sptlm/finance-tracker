package kfu.itis.maslennikov.financetracker.util;

import jakarta.xml.bind.DatatypeConverter;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class PasswordUtil {

    private PasswordUtil() {
    }

    public static String encrypt(String password){
        MessageDigest md;

        try {
            md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();

            return DatatypeConverter.printHexBinary(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean checkPassword(String password, String hashedPassword) {
        return encrypt(password).equals(hashedPassword);
    }
}