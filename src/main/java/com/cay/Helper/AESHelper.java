package com.cay.Helper;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class AESHelper {
	public static byte[] Encrypt(String sSrc, String sKey, String ivs){  
        if (sKey == null) {  
            System.out.print("Key为空null");  
            return null;  
        }  
        // 判断Key是否为16位  
        if (sKey.length() != 16) {  
            System.out.print("Key长度不是16位");  
            return null;  
        }  
        byte[] raw = sKey.getBytes();  
        byte[] ivb = ivs.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");  
        Cipher cipher;
        byte[] encrypted = null;
		try {
			cipher = Cipher.getInstance("AES/CBC/NoPadding");
			//"算法/模式/补码方式"  
	        IvParameterSpec iv = new IvParameterSpec(ivb);//使用CBC模式，需要一个向量iv，可增加加密算法的强度  
	        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);  
	       
	        byte[] srawt = sSrc.getBytes();
	        int len = srawt.length;
	        /* 计算补0后的长度 */
	        while(len % 16 != 0) len ++;
	        byte[] sraw = new byte[len];
	        /* 在最后补0 */
	        for (int i = 0; i < len; ++i) {
	            if (i < srawt.length) {
	                sraw[i] = srawt[i];
	            } else {
	                sraw[i] = 0;
	            }
	        }
	        encrypted = cipher.doFinal(sraw);  
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  
        return encrypted;//此处使用BASE64做转码功能，同时能起到2次加密的作用。  
    }  
  
    // 解密  
    public static byte[] Decrypt(byte[] sSrc, String sKey, String ivs){  
        try {  
            // 判断Key是否正确  
            if (sKey == null) {  
                System.out.print("Key为空null");  
                return null;  
            }  
            // 判断Key是否为16位  
            if (sKey.length() != 16) {  
                System.out.print("Key长度不是16位");  
                return null;  
            }  
            byte[] raw = sKey.getBytes("ASCII");  
            byte[] ivb = ivs.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");  
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");  
            IvParameterSpec iv = new IvParameterSpec(ivb);  
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);  
            byte[] encrypted1 = sSrc;
            try {  
                byte[] original = cipher.doFinal(encrypted1);  
                return original;  
            } catch (Exception e) {  
                System.out.println(e.toString());  
                return null;  
            }  
        } catch (Exception ex) {  
            System.out.println(ex.toString());  
            return null;  
        }  
    } 
}
