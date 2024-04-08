package com.atguigu.crowd.util;

import com.atguigu.crowd.constant.CrowdConstant;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CrowdUtil {

    public  static  String md5(String source){
        // 1.判断source是否有效
        if (source == null||source.length() ==0){
            // 2.如果不是有效字符抛出异常
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }

        try {
            // 3.获取MessageDigest对象
            String algorithm ="md5";

            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

            // 4.获取明文字符串的字节数组
            byte[] input =source.getBytes();

            // 5.执行加密
            byte[] output =messageDigest.digest(input);

            // 6.创建BigInteger对象
            int signum =1;
            BigInteger bigInteger =new BigInteger(signum,output);

            // 7.按照16进制将bigInteger的值转换为字符穿
            int radix =16;
            String encoded =bigInteger.toString(radix).toUpperCase();
            return  encoded;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断当前请求是否为 Ajax 请求
     * @param request * @return
     *                返回ture就是ajax请求 否则是普通请求
     */
    public static boolean judgeRequestType(HttpServletRequest request) {
// 1.获取请求消息头信息
        String acceptHeader = request.getHeader("Accept");
        String xRequestHeader = request.getHeader("X-Requested-With");
        // 2.检查并返回
        return
                (acceptHeader != null && acceptHeader.length() > 0 && acceptHeader.contains("application/json")) ||
                        (xRequestHeader != null && xRequestHeader.length() > 0 && xRequestHeader.equals("XMLHttpRequest"));
    }
}

