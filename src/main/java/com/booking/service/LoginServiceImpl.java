package com.booking.service;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.booking.common.service.IMailService;
import com.booking.domain.User;
import com.booking.dto.UserQueryDTO;
import com.booking.utils.CaptchaInfo;
import com.booking.utils.MailInfo;
import com.booking.utils.ResponseEntity;
import com.booking.utils.SHA2;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    CacheManager cacheManager;
    @Autowired
    UserService userService;
    @Autowired
    SHA2 sha;
    @Autowired
    IMailService mailService;

    public static final String EHCACHE_LOGIN = "login";
    public static final String EHCACHE_CAPTCHA = "captcha";
    public static final String EHCACHE_RSA = "rsa";

    public ResponseEntity<String> getPublicKey(String email){
        RSA rsa=new RSA();
        Element element=new Element("rsa_"+email,rsa);
        cacheManager.getCache(EHCACHE_RSA).put(element);
        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data(rsa.getPublicKeyBase64());
    }
    public ResponseEntity<CaptchaInfo> getCaptcha(String email) throws IOException, MessagingException {
        CaptchaInfo captchaInfo=new CaptchaInfo();
        StringBuffer code=new StringBuffer();

        for(int i=0;i<4;i++){
            code.append(sha.getRandomInt(10));
        }

        String token = UUID.randomUUID().toString();
        Element element=new Element("captcha_"+token,code.toString());
        cacheManager.getCache(EHCACHE_CAPTCHA).put(element);
        captchaInfo.setCode(code.toString());
        captchaInfo.setToken(token);

        MailInfo mailInfo=new MailInfo();
        mailInfo.setTo(email);
        mailInfo.setSubject("验证码");
        mailService.sendTemplateMail(mailInfo,"thymeleaf/verification.html","code",code);

        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data(captchaInfo);
    }
//    public ResponseEntity<CaptchaInfo> getCaptcha() throws IOException {
//        Cache cahce=cacheManager.getCache(EHCACHE_CAPTCHA);
//        CaptchaInfo cInfo=new CaptchaInfo();
//        //定义图形验证码的长和宽
//        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(126, 30,4,150);
//
//        String token = UUID.randomUUID().toString();
//        Element element=new Element("user_captcha_"+token,lineCaptcha.getCode());
//        cahce.put(element);
//        cInfo.setCode(lineCaptcha.getCode());
//        cInfo.setToken(token);
//
//        // 将图片转换为Base64字符串
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ImageIO.write(lineCaptcha.getImage(), "png", baos);//写入流中
//        byte[] bytes = baos.toByteArray();//转换成字节
//        BASE64Encoder encoder = new BASE64Encoder();
//        String png_base64 =  encoder.encodeBuffer(bytes).trim();//转换成base64串
//        //png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", "");//删除 \r\n
//        cInfo.setImg(png_base64);
//
//        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data(cInfo);
//    }
    public ResponseEntity<String> login(String password, String email) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Element element=cacheManager.getCache(EHCACHE_RSA).get("rsa_"+email);
        if(null!=element) {
            RSA rsa= (RSA) (element.getObjectValue());

            UserQueryDTO dto = new UserQueryDTO();
            dto.setEmail(email);
            User user = userService.findByEmail(UserQueryDTO.getWhereClause(dto));

            byte[] real = rsa.decrypt(Base64.decodeBase64(password), KeyType.PrivateKey);
            password = StrUtil.str(real, CharsetUtil.CHARSET_UTF_8);
            System.out.println("email:"+email);
            System.out.println("密码："+password);
            password = sha.sha2(password + user.getSalt());

            if (password.equals(user.getUpassword())&&user.getEnable()) {
                element=new Element(user.getUid(),user);
                cacheManager.getCache(EHCACHE_LOGIN).put(element);
                System.out.println("密码正确, uid:"+user.getUid());
                return ResponseEntity.ofSuccess().status(HttpStatus.OK).data(user.getUid());
            }
            System.out.println("密码错误/账号已停用");
        }
        return ResponseEntity.ofFailed();
    }
    public ResponseEntity<String> register(String password, String email, String captcha, String token) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if(!userService.existsByEmail(email)) {
            Element captchaEle = cacheManager.getCache(EHCACHE_CAPTCHA).get("captcha_" + token);
            Element rsaEle = cacheManager.getCache(EHCACHE_RSA).get("rsa_" + email);
            if (null != captchaEle && null != rsaEle) {
                if (captcha.equals(captchaEle.getObjectValue())) {
                    RSA rsa = (RSA) (rsaEle.getObjectValue());
                    byte[] real = rsa.decrypt(Base64.decodeBase64(password), KeyType.PrivateKey);
                    password = StrUtil.str(real, CharsetUtil.CHARSET_UTF_8);

                    String salt = sha.getSalt(128);
                    System.out.println("email:" + email);
                    System.out.println("password:" + password);
                    password = sha.sha2(password + salt);

                    User user = new User();
                    user.setUname("新用户");
                    user.setUpassword(password);
                    user.setIcon("/upload/user/avatar/default_avatar.jpeg");
                    user.setEnable(true);
                    user.setEmail(email);
                    user.setSalt(salt);

                    userService.save(user);
                    return ResponseEntity.ofSuccess().status(HttpStatus.OK).data("success");
                }
            }
        }
        return ResponseEntity.ofFailed();
    }
}
