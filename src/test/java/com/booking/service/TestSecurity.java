package com.booking.service;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.booking.utils.SHA2;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class TestSecurity {
    SHA2 security=new SHA2();
    @Test
    public void salt(){
        System.out.println(security.getSalt(128));
    }
    @Test
    public void sha2() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String password="12345678";
        String salt=security.getSalt(128);
        String encry=security.sha2(password+salt);
        System.out.println(encry);
        System.out.println(encry.length());
    }
    @Test
    public void rsa(){
        RSA rsa=new RSA();
        System.out.print("公钥:");
        System.out.println(rsa.getPublicKeyBase64());
        System.out.print("秘钥:");
        System.out.println(rsa.getPrivateKeyBase64());

        byte[] text=rsa.encrypt("abc", KeyType.PublicKey);
        String text2=Base64.encode(text);

        System.out.print("密码:");
        System.out.println(text2);

      //  System.out.println(Base64.decode(text2).length());

        byte[] real=rsa.decrypt(text2,KeyType.PrivateKey);

        System.out.println(StrUtil.str(real, CharsetUtil.CHARSET_UTF_8));
    }
}
