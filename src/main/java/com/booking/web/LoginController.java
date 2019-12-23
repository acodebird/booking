package com.booking.web;

import com.booking.domain.User;
import com.booking.service.LoginService;
import com.booking.utils.CaptchaInfo;
import org.springframework.beans.factory.annotation.Autowired;
import com.booking.utils.ResponseEntity;
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
        if(null!=email)
        {
            System.out.println("getPublicKey");
            return loginService.getPublicKey(email);
        }
        return ResponseEntity.ofFailed();
    }

    // 登录
    @PostMapping
    public ResponseEntity<String> login (@RequestBody User user) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        System.out.println("login");
        String email=user.getEmail();
        String password=user.getUpassword();
        if(null!=email&&null!=password){
            return loginService.login(password,email);
        }
        return ResponseEntity.ofFailed();
    }

    // 获取验证码
    @GetMapping(value="/register/{email}")
    public ResponseEntity<CaptchaInfo> getCaptcha(@PathVariable("email") String email) throws IOException, MessagingException {
        System.out.println("getCaptcha");
        if(null!=email){
            return loginService.getCaptcha(email);
        }
        return ResponseEntity.ofFailed();
    }

//    // 注册用户
//    @PostMapping(value="/register")
//    public ResponseEntity<String> register (@RequestBody String password, @RequestBody String email, @RequestBody String code, @RequestBody String token) throws UnsupportedEncodingException, NoSuchAlgorithmException {
//        System.out.println("register");
//        if(null!=password&&null!=email&&null!=code&&null!=token){
//            return loginService.register(password, email, code, token);
//        }
//        return ResponseEntity.ofFailed();
//    }
// 注册用户
@PostMapping(value="/register")
public ResponseEntity<String> register (User user,CaptchaInfo captchaInfo) throws UnsupportedEncodingException, NoSuchAlgorithmException {
    System.out.println("register");

    String password=user.getUpassword();
    String email=user.getEmail();
    String code=captchaInfo.getCode();
    String token=captchaInfo.getToken();
    if(null!=password&&null!=email&&null!=code&&null!=token){
        return loginService.register(password, email, code, token);
    }
    return ResponseEntity.ofFailed();
}
}
