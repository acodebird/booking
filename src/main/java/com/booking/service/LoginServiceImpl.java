package com.booking.service;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.booking.common.service.IMailService;
import com.booking.domain.User;
import com.booking.utils.CaptchaInfo;
import com.booking.common.domain.MailInfo;
import com.booking.utils.ResponseEntity;
import com.booking.utils.SHA2;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

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

    public static final String VAILD_CAPTCHA_SUCCESS = "1";
    public static final String LOGIN_SESSION_TOKEN = "user";
    public static final String EHCACHE_CAPTCHA = "captcha";
    public static final String EHCACHE_RSA = "rsa";
    public static final String EHCACHE_TEMP_PASSWORD = "temp_password";

    public ResponseEntity<String> getPublicKey(String email){
        RSA rsa=new RSA();
        Element element=new Element(email,rsa);
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
        Element element=new Element(token,code.toString());
        cacheManager.getCache(EHCACHE_CAPTCHA).put(element);
        captchaInfo.setCode(code.toString());
        captchaInfo.setToken(token);

        MailInfo mailInfo=new MailInfo();
        mailInfo.setTo(email);
        mailInfo.setSubject("验证码");
        //mailService.sendTemplateMail(mailInfo,"thymeleaf/verification.html","code",code);
        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data(captchaInfo);
    }

    public ResponseEntity<CaptchaInfo> getGraphCaptcha() throws IOException {
        //定义图形验证码的长和宽
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(85, 30,4,100);
        // 将图片转换为Base64字符串
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(lineCaptcha.getImage(), "png", baos);//写入流中
        byte[] bytes = baos.toByteArray();//转换成字节
        BASE64Encoder encoder = new BASE64Encoder();
        String png_base64 =  encoder.encodeBuffer(bytes).trim();//转换成base64串
        //png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", "");//删除 \r\n

        String token = UUID.randomUUID().toString();

        CaptchaInfo captchaInfo=new CaptchaInfo();
        captchaInfo.setCode(lineCaptcha.getCode());
        captchaInfo.setToken(token);
        captchaInfo.setImg("data:image/jpeg;base64,"+png_base64);

        Element element=new Element(token,lineCaptcha.getCode());
        cacheManager.getCache(EHCACHE_CAPTCHA).put(element);

        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data(captchaInfo);
    }

    public ResponseEntity<String> login(String password, String email, HttpSession session) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        System.out.println("email:"+email);
        User user = userService.findByEmail(email);
        if(null==user){
            return ResponseEntity.ofFailed().data("user_not_existed");
        }
        if(!user.getEnable()){
            return ResponseEntity.ofFailed().data("user_disable");
        }
        System.out.println("email:"+user.getEmail());

        password = decodePassword(password,EHCACHE_RSA,email);
        if (null == password) {
            return ResponseEntity.ofFailed().data("time_out");
        }

        System.out.println("密码："+password);
        password = sha.sha2(password + user.getSalt());

        if (!password.equals(user.getUpassword())) {
            Element element=cacheManager.getCache(EHCACHE_TEMP_PASSWORD).get(email);
            if(null==element||!password.equals((String)element.getObjectValue())){
                return ResponseEntity.ofFailed().data("password_error");
            }
        }

        session.setAttribute(LOGIN_SESSION_TOKEN,user);
//        element=new Element(user.getUid(),user);
//        cacheManager.getCache(EHCACHE_LOGIN).put(element);
        System.out.println("密码正确, uid:"+user.getUid());
        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data(user);
    }
    public ResponseEntity<String> logout(HttpSession session){
        session.removeAttribute(LOGIN_SESSION_TOKEN);
        return ResponseEntity.ofSuccess().data("logout");
    }
    public ResponseEntity<String> register(User user, String captcha, String token, int type) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if(userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.ofFailed().data("user_existed");
        }
        String password= user.getUpassword();
        String email= user.getEmail();
        if(password.trim().length()<1||email.trim().length()<1){
            return ResponseEntity.ofFailed().data("empty_info");
        }

        String vaildCaptcha=validCaptcha(captcha,EHCACHE_CAPTCHA,token);
        if(!VAILD_CAPTCHA_SUCCESS.equals(vaildCaptcha)){
            return ResponseEntity.ofFailed().data(vaildCaptcha);
        }

        password = decodePassword(password,EHCACHE_RSA,email);
        if (null == password) {
            return ResponseEntity.ofFailed().data("time_out");
        }

        System.out.println("email:" + email);
        System.out.println("password:" + password);
        System.out.println("type:" + type);
        String salt = sha.getSalt(128);
        password = sha.sha2(password + salt);

        User newUser = new User();
        newUser.setUpassword(password);
        newUser.setIcon("/upload/user/avatar/default_avatar.jpeg");
        newUser.setEnable(0==type?true:false);
        newUser.setEmail(email);
        newUser.setSalt(salt);
        newUser.setType(type);

        newUser.setUname(null==user.getUname()?"新用户":user.getUname());
        if(null!=user.getTelephone()){
            newUser.setTelephone(user.getTelephone());
        }

        userService.save(newUser);
        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data("success");
    }

    public ResponseEntity<String> forgot(String email, String code, String token) throws MessagingException, UnsupportedEncodingException, NoSuchAlgorithmException {
        System.out.println("email:" + email);

        String vaildCaptcha=validCaptcha(code,EHCACHE_CAPTCHA,token);
        if(!VAILD_CAPTCHA_SUCCESS.equals(vaildCaptcha)){
            return ResponseEntity.ofFailed().data(vaildCaptcha);
        }

        User user = userService.findByEmail(email);

        if(null==user){
            return ResponseEntity.ofFailed().data("user_not_existed");
        }

        String password=getRandomPassword();

        String encryptPassword=password+user.getSalt();
        encryptPassword=sha.sha2(encryptPassword);

        Element element=new Element(email,encryptPassword);
        cacheManager.getCache(EHCACHE_TEMP_PASSWORD).put(element);

        Map<String,Object> variable=new HashMap<String,Object>();
        variable.put("password",password);
        variable.put("uname",user.getUname());

        MailInfo mailInfo=new MailInfo();
        mailInfo.setTo(email);
        mailInfo.setSubject("找回密码");
        mailService.sendTemplateMail(mailInfo,"thymeleaf/forgottenPassword.html",variable);

        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data("success");
    }

    public ResponseEntity<String> changePassword(User user, String password, String newPassword, String code, String token) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String vaildCaptcha=validCaptcha(code,EHCACHE_CAPTCHA,token);
        if(!VAILD_CAPTCHA_SUCCESS.equals(vaildCaptcha)){
            return ResponseEntity.ofFailed().data(vaildCaptcha);
        }

        String email=user.getEmail();
        password = decodePassword(password,EHCACHE_RSA,email);
        newPassword = decodePassword(newPassword,EHCACHE_RSA,email);
        if (null == password||null==newPassword) {
            return ResponseEntity.ofFailed().data("time_out");
        }

        if (!password.equals(user.getUpassword())) {
            Element element=cacheManager.getCache(EHCACHE_TEMP_PASSWORD).get(email);
            if(null==element||!password.equals((String)element.getObjectValue())){
                return ResponseEntity.ofFailed().data("password_error");
            }
        }

        newPassword=sha.sha2(newPassword+user.getSalt());
        user.setUpassword(newPassword);
        userService.save(user);

        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data("success");
    }

    private String validCaptcha(final String captcha,final String cacheName,final String key){
        Element element=cacheManager.getCache(cacheName).get(key);
        if (null == element) {
            return "captcha_expired";
        }
        if (!captcha.equals(element.getObjectValue())) {
            return "captcha_error";
        }
        cacheManager.getCache(EHCACHE_CAPTCHA).remove(key);
        return VAILD_CAPTCHA_SUCCESS;
    }

    private String getRandomPassword(){
        StringBuffer password=new StringBuffer();
        int temp=0;
        for(int i=0;i<8;){
            temp=sha.getRandomInt(75);
            if((temp>9&&temp<17)||(temp>42&&temp<49)){
                continue;
            }
            i++;
            temp+=48;
            password.append((char)temp);
        }
        return password.toString();
    }

    private String decodePassword(final String password,final String cacheName,final String key){
        Element element = cacheManager.getCache(cacheName).get(key);
        if (null == element) {
            return null;
        }
        RSA rsa = (RSA) (element.getObjectValue());
        byte[] real = rsa.decrypt(Base64.decodeBase64(password), KeyType.PrivateKey);
        return StrUtil.str(real, CharsetUtil.CHARSET_UTF_8);
    }
}
