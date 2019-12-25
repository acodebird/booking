package com.booking.web;

import com.booking.domain.User;
import com.booking.service.LoginService;
import com.booking.service.LoginServiceImpl;
import com.booking.utils.CaptchaInfo;
import org.springframework.beans.factory.annotation.Autowired;
import com.booking.utils.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    // 获取RSA公钥
    @GetMapping(value="/isLogin")
    public ResponseEntity<String> isLogin (HttpSession session) {
        System.out.println("isLogin");
        return ResponseEntity.ofSuccess().status(HttpStatus.BAD_REQUEST).data((User)session.getAttribute(LoginServiceImpl.LOGIN_SESSION_TOKEN));
    }

    // 获取RSA公钥
    @GetMapping(value="/{email}")
    public ResponseEntity<String> getPublicKey (@PathVariable("email") String email) {
        System.out.println("getPublicKey");
        if(null==email){
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("parameter_error");
        }
        return loginService.getPublicKey(email);
    }

    // 登录
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

    // 登录
    @DeleteMapping
    public ResponseEntity<String> logout (HttpSession session) {
        System.out.println("logout");
        return loginService.logout(session);
    }

    // 获取邮箱验证码
    @GetMapping(value="/register/{email}")
    public ResponseEntity<CaptchaInfo> getCaptcha(@PathVariable("email") String email) throws IOException, MessagingException {
        System.out.println("getCaptcha");
        if(null==email) {
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("parameter_error");
        }
        return loginService.getCaptcha(email);
    }

    // 获取图形验证码
    @GetMapping(value="/register/")
    public ResponseEntity<CaptchaInfo> getGraphCaptcha() throws IOException, MessagingException {
        System.out.println("getGraphCaptcha");
        return loginService.getGraphCaptcha();
    }

    // 注册用户
    @PostMapping(value="/register")
    public ResponseEntity<String> register (@RequestBody User user, @RequestHeader("code") String code, @RequestHeader("token") String token, HttpSession session) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        System.out.println("register");
        if(null==user||null==user.getUpassword()||null==user.getEmail()||null==code||null==token){
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("parameter_error");
        }
        User loginUser= (User) session.getAttribute(LoginServiceImpl.LOGIN_SESSION_TOKEN);
        return loginService.register(user, code, token, null==loginUser?0:loginUser.getType());
    }

    // 忘记密码
    @PostMapping(value="/register/{email}")
    public ResponseEntity<String> forgot (@PathVariable("email") String email, @RequestHeader("code") String code, @RequestHeader("token") String token) throws UnsupportedEncodingException, NoSuchAlgorithmException, MessagingException {
        System.out.println("forgot");
        if(null==email||null==code||null==token){
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("parameter_error");
        }
        return loginService.forgot(email, code, token);
    }
    // 修改密码
    @PutMapping(value="/register")
    public ResponseEntity<String> changePassword (@RequestBody Map<String,String> info, @RequestHeader("code") String code, @RequestHeader("token") String token, HttpSession session) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        System.out.println("changePassword");
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
        return loginService.changePassword(user, password, newPassword, code, token);
    }
}
