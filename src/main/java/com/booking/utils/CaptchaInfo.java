package com.booking.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CaptchaInfo {
    private String token;
    private String code;
    private String img;

    @Override
    public String toString() {
        return "CaptchaInfo{" +
                "token='" + token + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
