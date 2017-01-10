package com.cay.Helper;

import java.security.MessageDigest;

import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;  

public class ParamUtils{
	private static final String appSecret = "08c53213c0b3e974a7ea47929b88ca05";
	private static final String appKey = "3b1445feed4b";	
	private static final String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";  
	private static final String LETTERCHAR = "abcdefghijkllmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";  
	private static final String NUMBERCHAR = "0123456789"; 
    private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    
	/**
     *      * 创建分页请求.      
     */
    public static PageRequest buildPageRequest(int pageNumber, int pageSize,String sortType, String sorts) {
        Sort sort = null;
        if ("".equals(sortType)) {
            sort = new Sort(Direction.ASC, "id");
        } else {
            sort = new Sort(Direction.ASC, sorts);
        }
        //参数1表示当前第几页,参数2表示每页的大小,参数3表示排序
        return new PageRequest(pageNumber-1,pageSize,sort);
    }

    // 计算并获取md5值
    public static String getMD5(String requestBody) {
        return encode("md5", requestBody);
    }

    private static String encode(String algorithm, String value) {
        if (value == null) {
            return null;
        }
        try {
            MessageDigest messageDigest
                    = MessageDigest.getInstance(algorithm);
            messageDigest.update(value.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }
    /** 
     * 返回一个定长的随机数字字符串(只包含数字) 
     *  
     * @param length 
     *            随机字符串长度 
     * @return 随机字符串 
     */  
    public static String generateNumber(int length) {  
        StringBuffer sb = new StringBuffer();  
        Random random = new Random();  
        for (int i = 0; i < length; i++) {  
            sb.append(NUMBERCHAR.charAt(random.nextInt(NUMBERCHAR.length())));  
        }  
        return sb.toString();  
    }  
    
    /** 
     * 返回一个定长的随机字符串(只包含大小写字母、数字) 
     *  
     * @param length 
     *            随机字符串长度 
     * @return 随机字符串 
     */  
    public static String generateString(int length) {  
        StringBuffer sb = new StringBuffer();  
        Random random = new Random();  
        for (int i = 0; i < length; i++) {  
            sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));  
        }  
        return sb.toString();  
    }  
  
    /** 
     * 返回一个定长的随机纯字母字符串(只包含大小写字母) 
     *  
     * @param length 
     *            随机字符串长度 
     * @return 随机字符串 
     */  
    public static String generateMixString(int length) {  
        StringBuffer sb = new StringBuffer();  
        Random random = new Random();  
        for (int i = 0; i < length; i++) {  
            sb.append(ALLCHAR.charAt(random.nextInt(LETTERCHAR.length())));  
        }  
        return sb.toString();  
    }  
  
    /** 
     * 返回一个定长的随机纯大写字母字符串(只包含大小写字母) 
     *  
     * @param length 
     *            随机字符串长度 
     * @return 随机字符串 
     */  
    public static String generateLowerString(int length) {  
        return generateMixString(length).toLowerCase();  
    }  
  
    /** 
     * 返回一个定长的随机纯小写字母字符串(只包含大小写字母) 
     *  
     * @param length 
     *            随机字符串长度 
     * @return 随机字符串 
     */  
    public static String generateUpperString(int length) {  
        return generateMixString(length).toUpperCase();  
    }  
  
    /** 
     * 生成一个定长的纯0字符串 
     *  
     * @param length 
     *            字符串长度 
     * @return 纯0字符串 
     */  
    public static String generateZeroString(int length) {  
        StringBuffer sb = new StringBuffer();  
        for (int i = 0; i < length; i++) {  
            sb.append('0');  
        }  
        return sb.toString();  
    }  
  
    /** 
     * 根据数字生成一个定长的字符串，长度不够前面补0 
     *  
     * @param num 
     *            数字 
     * @param fixdlenth 
     *            字符串长度 
     * @return 定长的字符串 
     */  
    public static String toFixdLengthString(long num, int fixdlenth) {  
        StringBuffer sb = new StringBuffer();  
        String strNum = String.valueOf(num);  
        if (fixdlenth - strNum.length() >= 0) {  
            sb.append(generateZeroString(fixdlenth - strNum.length()));  
        } else {  
            throw new RuntimeException("将数字" + num + "转化为长度为" + fixdlenth  
                    + "的字符串发生异常！");  
        }  
        sb.append(strNum);  
        return sb.toString();  
    }  
  
    /** 
     * 每次生成的len位数都不相同 
     *  
     * @param param 
     * @return 定长的数字 
     */  
    public static int getNotSimple(int[] param, int len) {  
        Random rand = new Random();  
        for (int i = param.length; i > 1; i--) {  
            int index = rand.nextInt(i);  
            int tmp = param[index];  
            param[index] = param[i - 1];  
            param[i - 1] = tmp;  
        }  
        int result = 0;  
        for (int i = 0; i < len; i++) {  
            result = result * 10 + param[i];  
        }  
        return result;  
    }  
}
