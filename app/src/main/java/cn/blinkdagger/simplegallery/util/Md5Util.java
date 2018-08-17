package cn.blinkdagger.simplegallery.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {

    //字符数组与十六进制的转换
    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static String  getMd5Value(String url){
        String md5Value;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(url.getBytes());
            md5Value = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            md5Value = String.valueOf(url.hashCode());
        }
        return md5Value;
    }
}
