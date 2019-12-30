package com.booking.web;

import com.booking.domain.User;
import com.booking.service.LoginService;
import com.booking.service.LoginServiceImpl;
import com.booking.service.UserService;
import com.booking.utils.CaptchaInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.booking.utils.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    LoginService loginService;
    @Autowired
    UserService userService;

    // 用户是否登录
    // email:用户email
    // return:返回储存rsa公钥的响应实体ResponseEntity<String>
    @GetMapping(value="/isLogin")
    public ResponseEntity<String> isLogin (HttpSession session) throws Exception {
        System.out.println("isLogin");
        User user=(User)session.getAttribute(LoginServiceImpl.LOGIN_SESSION_TOKEN);
        throw new Exception("返回储存rsa公钥的响应实体ResponseEntity<String>");
//        if(null==user){
//            System.out.println("not login");
//            return ResponseEntity.ofFailed().data("user_not_login");
//        }
//        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data(user);
    }

    // 获取RSA加密公钥
    // email:用户email
    // return:如果登录则返回用户信息User, 否则返回储存相关代码(views/utils/errorTips.js)的响应实体ResponseEntity<String>
    @GetMapping(value="/{email}")
    public ResponseEntity<String> getPublicKey (@PathVariable("email") String email) {

        System.out.println("getPublicKey");
        if(null==email){
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("parameter_error");
        }
        return loginService.getPublicKey(email);
    }

    // 登录
    // user:包含用户邮箱和密码的用户信息
    // return:返回储存相关代码(views/utils/errorTips.js)的响应实体ResponseEntity<String>
    @PostMapping
    public ResponseEntity<String> login (@RequestBody User user, HttpSession session) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        System.out.println("login");
        String email=user.getEmail();
        String password=user.getUpassword();
        if(null==email||null==password){
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("parameter_error");
        }
        return loginService.login(password,email, session);
    }

    // 登出
    // return:返回储存相关代码(views/utils/errorTips.js)的响应实体ResponseEntity<String>
    @DeleteMapping
    public ResponseEntity<String> logout (HttpSession session) {
        System.out.println("logout");
        return loginService.logout(session);
    }

    // 获取邮箱验证码
    // return:返回储存验证码和验证码token的响应实体ResponseEntity<CaptchaInfo>
    @GetMapping(value="/register/{email}")
    public ResponseEntity<CaptchaInfo> getCaptcha(@PathVariable("email") String email) throws IOException, MessagingException {
        System.out.println("getCaptcha");
        if(null==email) {
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("parameter_error");
        }
        return loginService.getCaptcha(email);
    }

    // 获取图形验证码
    // return:返回储存验证码、验证码token和验证码图片的响应实体ResponseEntity<CaptchaInfo>
    @GetMapping(value="/register/")
    public ResponseEntity<CaptchaInfo> getGraphCaptcha() throws IOException, MessagingException {
        System.out.println("getGraphCaptcha");
        return loginService.getGraphCaptcha();
    }

    // 注册用户
    // user:包含email和password的用户信息
    // code:验证码
    // token:获取服务器上的验证码的token
    // return:返回储存相关代码(views/utils/errorTips.js)的响应实体ResponseEntity<String>
    @PostMapping(value="/register")
    public ResponseEntity<String> register (@RequestBody User user, @RequestHeader("code") String code, @RequestHeader("token") String token, HttpSession session) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        System.out.println("register");
        if(null==user||null==user.getUpassword()||null==user.getEmail()||null==code||null==token){
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("parameter_error");
        }
        User loginUser= (User) session.getAttribute(LoginServiceImpl.LOGIN_SESSION_TOKEN);
        return loginService.register(user, code, token, null==loginUser?0:loginUser.getType());
    }

    // 找回密码
    // email:要找回密码的email
    // code:验证码
    // token:获取服务器上的验证码的token
    // return:返回储存相关代码(views/utils/errorTips.js)的响应实体ResponseEntity<String>
    @PostMapping(value="/register/{email}")
    public ResponseEntity<String> forgot (@PathVariable("email") String email, @RequestHeader("code") String code, @RequestHeader("token") String token) throws UnsupportedEncodingException, NoSuchAlgorithmException, MessagingException {
        System.out.println("forgot");
        if(null==email||null==code||null==token){
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("parameter_error");
        }
        return loginService.forgot(email, code, token);
    }

    //验证邮箱是否可用
    @PostMapping("/update/info")
    public ResponseEntity<String> vaildEmailFunction(@RequestHeader("code") String code, @RequestHeader("token") String token) {
        System.out.println("vaildEmailFunction");
        if(null==code||null==token){
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("parameter_error");
        }
        return loginService.vaildEmailFunction(code,token);
    }

    // 更新个人信息
    // user:包含用户更新后的所有信息
    // return:返回储存相关代码(views/utils/errorTips.js)的响应实体ResponseEntity<String>
    @PutMapping("/update/info")
    public ResponseEntity<String> updateInfo(@RequestBody User user,HttpSession session){
        System.out.println("updateInfo");
        if(null==user){
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("parameter_error");
        }
        User target = (User) session.getAttribute(LoginServiceImpl.LOGIN_SESSION_TOKEN);
        if(target==null||target.getUid()!=user.getUid()) {
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("user_not_login");
        }
        String email=user.getEmail();
        if(!target.getEmail().equals(email)){
            if(userService.existsByEmail(email)){
                return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("email_existed");
            }
        }
        BeanUtils.copyProperties(user, target);
        return loginService.updateInfo(target);
    }

    // 修改密码
    // info:包含rsa加密后的用户旧/新密码
    // code:修改密码的验证码
    // token:获取服务器上的验证码的token
    // return:返回储存相关代码(views/utils/errorTips.js)的响应实体ResponseEntity<String>
    @PutMapping(value="/update/password")
    public ResponseEntity<String> updatePassword (@RequestBody Map<String,String> info, @RequestHeader("code") String code, @RequestHeader("token") String token, HttpSession session) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        System.out.println("updatePassword");
        User user=(User)session.getAttribute(LoginServiceImpl.LOGIN_SESSION_TOKEN);
        if(null==user){
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("user_not_login");
        }
        if(null==info||null==code||null==token){
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("parameter_error");
        }
        String password=info.get("password");
        String newPassword=info.get("newPassword");
        if(null==password||null==newPassword){
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("parameter_error");
        }
        return loginService.updatePassword(user, password, newPassword, code, token);
    }

    // 修改头像
    // multipartFile:用户新头像
    // code:修改密码的验证码
    // token:获取服务器上的验证码的token
    // return:返回储存相关代码(views/utils/errorTips.js)的响应实体ResponseEntity<String>
    @PutMapping(value="/update/avatar")
    public ResponseEntity<String> updateAvatar (@RequestBody MultipartFile multipartFile, HttpSession session) throws IOException, NoSuchAlgorithmException {
        System.out.println("updateAvatar");
        User user=(User)session.getAttribute(LoginServiceImpl.LOGIN_SESSION_TOKEN);
        if(null==user){
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("user_not_login");
        }
        if(null==multipartFile){
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("parameter_error");
        }
        return loginService.updateAvatar(multipartFile,user);
    }
}
