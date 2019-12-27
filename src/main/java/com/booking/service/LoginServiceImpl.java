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
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.File;
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

    public static final String VAILD_SUCCESS = "1";
    public static final String LOGIN_SESSION_TOKEN = "user";
    public static final String EHCACHE_CAPTCHA = "captcha";
    public static final String EHCACHE_RSA = "rsa";
    public static final String EHCACHE_TEMP_PASSWORD = "temp_password";

    public static final String PATH=ClassUtils.getDefaultClassLoader().getResource("").getPath();
    public static final String STATIC="static";
    public static final String AVATAR_PATH="/upload/user/avatar/";

    // 获取rsa公钥
    // email:要获取rsa公钥的用户email
    // return:返回储存rsa公钥的响应实体ResponseEntity<String>
    public ResponseEntity<String> getPublicKey(String email){
        RSA rsa=new RSA();
        Element element=new Element(email,rsa);
        cacheManager.getCache(EHCACHE_RSA).put(element);
        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data(rsa.getPublicKeyBase64());
    }

    // 获取邮件验证码
    // email:要获取邮箱验证码的用户email
    // return:返回储存验证信息(验证码和验证码token)的响应实体ResponseEntity<CaptchaInfo>
    public ResponseEntity<CaptchaInfo> getCaptcha(String email) throws MessagingException {
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
        mailInfo.setSubject("注册验证码");
        mailService.sendTemplateMail(mailInfo,"verification.html","code",code.toString());

        System.out.println("邮箱验证码:"+code);

        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data(captchaInfo);
    }

    // 获取图形验证信息
    // return:返回储存验证信息(验证码, 验证码图片, 验证码token)的响应实体ResponseEntity<CaptchaInfo>
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

        System.out.println("图片验证码:"+lineCaptcha.getCode());

        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data(captchaInfo);
    }

    // 用户登录
    // password:用户密码
    // email:用户email
    // return:返回储存相关代码(views/utils/errorTips.js)的响应实体ResponseEntity<String>
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

        String vaild=validPassword(user.getUpassword(), password, EHCACHE_TEMP_PASSWORD,email);
        if(!VAILD_SUCCESS.equals(vaild)){
            return ResponseEntity.ofFailed().data(vaild);
        }

        session.setAttribute(LOGIN_SESSION_TOKEN,user);
//        element=new Element(user.getUid(),user);
//        cacheManager.getCache(EHCACHE_LOGIN).put(element);
        System.out.println("密码正确, uid:"+user.getUid());
        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data(user);
    }

    // 用户登出
    // return:返回储存相关代码(views/utils/errorTips.js)的响应实体ResponseEntity<String>
    public ResponseEntity<String> logout(HttpSession session){
        session.removeAttribute(LOGIN_SESSION_TOKEN);
        return ResponseEntity.ofSuccess().data("logout");
    }

    // 用户注册
    // user:用户信息(最少包含email和password)
    // captcha:邮箱验证码
    // token:验证码token
    // type:要注册的用户类型
    // return:返回储存相关代码(views/utils/errorTips.js)的响应实体ResponseEntity<String>
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
        if(!VAILD_SUCCESS.equals(vaildCaptcha)){
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

    // 找回密码
    // email:用户email
    // code:邮箱验证码
    // token:验证码token
    // return:返回储存相关代码(views/utils/errorTips.js)的响应实体ResponseEntity<String>
    public ResponseEntity<String> forgot(String email, String code, String token) throws MessagingException, UnsupportedEncodingException, NoSuchAlgorithmException {
        System.out.println("email:" + email);

        String vaildCaptcha=validCaptcha(code,EHCACHE_CAPTCHA,token);
        if(!VAILD_SUCCESS.equals(vaildCaptcha)){
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
        mailService.sendTemplateMail(mailInfo,"forgottenPassword.html",variable);

        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data("success");
    }

    // 更换密码
    // user:本地(服务器)用户信息
    // password:正在使用的密码
    // newPassword:新密码
    // code:邮箱验证码
    // token:验证码token
    // return:返回储存相关代码(views/utils/errorTips.js)的响应实体ResponseEntity<String>
    public ResponseEntity<String> updatePassword(User user, String password, String newPassword, String code, String token) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String vaild=validCaptcha(code,EHCACHE_CAPTCHA,token);
        if(!VAILD_SUCCESS.equals(vaild)){
            return ResponseEntity.ofFailed().data(vaild);
        }

        String email=user.getEmail();
        password = decodePassword(password,EHCACHE_RSA,email);
        newPassword = decodePassword(newPassword,EHCACHE_RSA,email);
        if (null == password||null==newPassword) {
            return ResponseEntity.ofFailed().data("time_out");
        }

        password=sha.sha2(password+user.getSalt());
        vaild=validPassword(user.getUpassword(), password, EHCACHE_TEMP_PASSWORD,email);
        if(!VAILD_SUCCESS.equals(vaild)){
            return ResponseEntity.ofFailed().data(vaild);
        }

        newPassword=sha.sha2(newPassword+user.getSalt());
        user.setUpassword(newPassword);
        userService.save(user);

        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data("success");
    }

    // 验证邮箱是否可用
    // code:邮箱验证码
    // token:验证码token
    // return:返回储存相关代码(views/utils/errorTips.js)的响应实体ResponseEntity<String>
    public ResponseEntity<String> vaildEmailFunction(String code, String token){
        String vaildCaptcha=validCaptcha(code,EHCACHE_CAPTCHA,token);
        if(!VAILD_SUCCESS.equals(vaildCaptcha)){
            return ResponseEntity.ofFailed().data(vaildCaptcha);
        }
        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data("success");
    }

    // 更新个人信息
    // user:带有更新信息的User
    // return:返回储存相关代码(views/utils/errorTips.js)的响应实体ResponseEntity<String>
    public ResponseEntity<String> updateInfo(User user){
        userService.save(user);
        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data("success");
    }

    // 更新头像
    // multipartFile:用户上传的头像
    // user:登录用户的用户信息
    // return:返回储存相关代码(views/utils/errorTips.js)的响应实体ResponseEntity<String>
    public ResponseEntity<String> updateAvatar(MultipartFile multipartFile, User user) throws IOException {
        System.out.println(multipartFile.getOriginalFilename());
        String parentPath=PATH+STATIC;
        String path=AVATAR_PATH+user.getUid()+getExtense(multipartFile.getOriginalFilename());
        multipartFile.transferTo(new File(parentPath+path));
        user.setIcon(path);
        userService.save(user);
        System.out.print("头像保存为:");
        System.out.println(parentPath+path);
        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data("success");
    }

    // 检查验证码是否正确
    // captcha:用户输入的验证码
    // cacheName:本地存储验证码的cache名
    // key:cache上验证码对应的key
    // return:如果验证码正确则返回LoginServiceImpl.VAILD_SUCCESS 否则返回相关错误代码(views/utils/errorTips.js)
    private String validCaptcha(final String captcha,final String cacheName,final String key){
        Element element=cacheManager.getCache(cacheName).get(key);
        if (null == element) {
            return "captcha_expired";
        }
        if (!captcha.equals(element.getObjectValue())) {
            return "captcha_error";
        }
        cacheManager.getCache(EHCACHE_CAPTCHA).remove(key);
        return VAILD_SUCCESS;
    }

    // 检查密码是否正确
    // target:正确的被sha2加密后的密码(password+salt)
    // password:用户输入的被sha2加密后的密码
    // cacheName:本地存临时密码的cache名
    // key:cache上临时密码对应的key
    // return:如果密码正确则返回LoginServiceImpl.VAILD_SUCCESS 否则返回相关错误代码(views/utils/errorTips.js)
    private String validPassword(final String target, String password, final String cacheName,final String key) {
        if (!password.equals(target)) {
            Element element=cacheManager.getCache(cacheName).get(key);
            if(null==element||!password.equals((String)element.getObjectValue())){
                return "password_error";
            }
        }
        return VAILD_SUCCESS;
    }

    // 获取随机密码
    // return:返回随机密码String
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
    // 解码被rsa加密后的文本
    // password:被rsa加密后的文本
    // cacheName:本地存储RSA对象的cache名
    // key:cache上RSA对象对应的key
    // return:如果解码成功返回被解码的文本, 否则返回null
    private String decodePassword(final String password,final String cacheName,final String key){
        Element element = cacheManager.getCache(cacheName).get(key);
        if (null == element) {
            return null;
        }
        RSA rsa = (RSA) (element.getObjectValue());
        byte[] real = rsa.decrypt(Base64.decodeBase64(password), KeyType.PrivateKey);
        return StrUtil.str(real, CharsetUtil.CHARSET_UTF_8);
    }

    // 获取文件扩展名 例如(.jpg)
    // fileName:文件名
    // return:如果有扩展名则返回扩展名 否则返回空字符串
    private String getExtense(final String fileName){
        int lastIndex=fileName.lastIndexOf('.');
        return lastIndex<0?"":fileName.substring(lastIndex);
    }
}
