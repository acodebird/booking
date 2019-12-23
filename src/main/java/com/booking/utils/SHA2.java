package com.booking.utils;

import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Component
public class SHA2 {
    public final static String SHA2_VERSION="SHA-512";
    private Random random=new Random();

    // 获取一个对 rmain 求余后的随机正整数
    public int getRandomInt(final int remain){
        int r=random.nextInt()%remain;
        return r>0?r:-r;
    }

    // 获取字符盐
    public String getSalt(final int len){

        StringBuffer bufStrSalt=new StringBuffer();
        int iRandomNum=0;
        for(int i=len;i>0;i--) {
            iRandomNum=this.getRandomInt(94)+33;
            bufStrSalt.append((char)iRandomNum);
        }
        return bufStrSalt.toString();
    }

    // 获取SHA2(SHA-512)加密过的字符串
    public String sha2(final String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        BigInteger biEncryptedText=new BigInteger((MessageDigest.getInstance(SHA2_VERSION)).digest(text.getBytes("utf-8")));
        return biEncryptedText.toString(16);
    }
}
