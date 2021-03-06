package com.cay.Helper;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.spec.IvParameterSpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


public class AESHelper {
	
    // 加密
    public static String encrypt(final byte[] data, String key, String iv) throws Exception {
    	String res = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] keys = key.getBytes("utf-8");
            byte[] ivs = iv.getBytes("utf-8");
            SecretKey secretKey = new SecretKeySpec(keys, "AES");
            Base64 b64 = new Base64();            
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(ivs));//使用加密模式初始化 密钥
            byte[] decrypt = cipher.doFinal(data);
            byte[] encrypt = b64.encode(decrypt);
            res = new String(encrypt);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return res;
    }
    
    /**
     * 解密.
     * @param data
     *          解密的数据.
     * @param key
     *          密钥.
     * @param iv
     *          CBC算法所需初始矩阵.
     * @return 解密结果.
     */
    public static String decrypt(
            final byte[] data, String key, String iv
    ) {
        String res = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] keys = key.getBytes("utf-8");
            byte[] ivs = iv.getBytes("utf-8");
            SecretKey secretKey = new SecretKeySpec(keys, "AES");
            Base64 b64 = new Base64();
            byte[] encrypt = b64.decode(data);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(ivs));//使用解密模式初始化 密钥
            byte[] decrypt = cipher.doFinal(encrypt);
            res = new String(decrypt);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return res;
    }
}
