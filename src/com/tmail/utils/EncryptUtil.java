package com.tmail.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by tobin on 12/11/14.
 */
public class EncryptUtil {

    /**
     * change byte[] to hex string
     *
     * @param str
     * @return
     */
    private static String hex(byte[] str) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length; i++) {
            String pos = Integer.toHexString((str[i] & 0xFF) | 0x100).substring(1, 3);
            sb.append(pos);
        }
        return sb.toString();
    }

    /**
     * using specified algorithm to encrypt text
     *
     * @param text
     * @param algorithm
     * @return
     */
    private static String encrypt(String text, String algorithm) {
        // ensure the suitable text
        if (text == null || "".equals(text.trim())) {
            throw new IllegalArgumentException("Please input the encrypt content!");
        }

        // default to use md5
        if (algorithm == null || "".equals(algorithm.trim())) {
            algorithm = "md5";
        }

        // encrypt text
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(text.getBytes("UTF8"));
            // get the encrypt bytes
            byte[] str = md.digest();
            // change to hex string
            String res = hex(str);
            return res;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * using sha-1 algorithm to encrypt text
     *
     * @param text
     * @return
     */
    public static String sha1(String text) {
        return encrypt(text, "sha-1");
    }
}
