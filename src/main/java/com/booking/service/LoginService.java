package com.booking.service;

import com.booking.domain.User;
import com.booking.utils.CaptchaInfo;
import com.booking.utils.ResponseEntity;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public interface LoginService {
    public ResponseEntity<String> getPublicKey(String email);
    public ResponseEntity<CaptchaInfo> getCaptcha(String email) throws IOException, MessagingException;
    public ResponseEntity<CaptchaInfo> getGraphCaptcha() throws IOException;
    public ResponseEntity<String> login(String password, String email, HttpSession session) throws UnsupportedEncodingException, NoSuchAlgorithmException;
    public ResponseEntity<String> logout(HttpSession session);
    public ResponseEntity<String> register(User user, String captcha, String token, int type) throws UnsupportedEncodingException, NoSuchAlgorithmException;
    public ResponseEntity<String> forgot(String account, String code, String token) throws MessagingException, UnsupportedEncodingException, NoSuchAlgorithmException;
    public ResponseEntity<String> changePassword(User user, String password, String newPassword, String code, String token) throws UnsupportedEncodingException, NoSuchAlgorithmException;
    public ResponseEntity<String> update(User user);
}
