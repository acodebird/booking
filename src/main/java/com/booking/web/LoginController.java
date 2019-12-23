package com.booking.web;

import com.booking.domain.User;
import com.booking.service.LoginService;
import com.booking.utils.CaptchaInfo;
import org.springframework.beans.factory.annotation.Autowired;
import com.booking.utils.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    LoginService loginService;

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
    public ResponseEntity<String> login (@RequestBody User user) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        System.out.println("login");
        String email=user.getEmail();
        String password=user.getUpassword();
        if(null==email||null==password){
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("parameter_error");
        }
        return loginService.login(password,email);
    }

    // 获取验证码
    @GetMapping(value="/register/{email}")
    public ResponseEntity<CaptchaInfo> getCaptcha(@PathVariable("email") String email) throws IOException, MessagingException {
        System.out.println("getCaptcha");
        if(null==email) {
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("parameter_error");
        }
        return loginService.getCaptcha(email);
    }

    // 注册用户
    @PostMapping(value="/register")
    public ResponseEntity<String> register (@RequestBody User user, @RequestHeader String code, @RequestHeader String token) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        System.out.println("register");
        if(null==user||null==user.getUpassword()||null==user.getEmail()||null==code||null==token){
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("parameter_error");
        }
        return loginService.register(user, code, token, 0);
    }
//    // 注册用户
//    @PostMapping(value="/register")
//    public ResponseEntity<String> register (@RequestBody User user, @RequestBody CaptchaInfo captchaInfo) throws UnsupportedEncodingException, NoSuchAlgorithmException {
//        System.out.println("register");
//        String password=user.getUpassword();
//        String email=user.getEmail();
//        String code=captchaInfo.getCode();
//        String token=captchaInfo.getToken();
//        if(null!=password&&null!=email&&null!=code&&null!=token){
//            return loginService.register(password, email, code, token);
//        }
//        return ResponseEntity.ofFailed();
//    }
}
